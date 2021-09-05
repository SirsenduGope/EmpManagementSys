package com.management.employee.serviceImpl;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.management.employee.entity.Designation;
import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.EmployeeStatus;
import com.management.employee.entity.Role;
import com.management.employee.enums.Roles;
import com.management.employee.payload.EmployeeDetailsRequest;
import com.management.employee.payload.Message;
import com.management.employee.payload.SignupRequest;
import com.management.employee.repository.DesignationRepository;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.repository.EmployeeStatusRepository;
import com.management.employee.repository.RoleRepository;
import com.management.employee.service.IEmployeeService;
import com.management.employee.utils.Helper;

import javassist.NotFoundException;

@Service
public class EmployeeServiceImpl implements IEmployeeService{
	
	private EmployeeRepository empRepo;
	private PasswordEncoder encoder;
	private RoleRepository roleRepository;
	private DesignationRepository designationRepository;
	private EmployeeStatusRepository empStatusRepository;
	
	public EmployeeServiceImpl(final EmployeeRepository empRepo,
			final PasswordEncoder encoder,
			final RoleRepository roleRepository,
			DesignationRepository designationRepository,
			EmployeeStatusRepository empStatusRepository) {
		this.empRepo = empRepo;
		this.encoder = encoder;
		this.roleRepository = roleRepository;
		this.designationRepository = designationRepository;
		this.empStatusRepository = empStatusRepository;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	
	@Override
	public ResponseEntity<?> getAllEmployee() {
		String loggedInUserEmailId = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		Optional<List<Employee>> employees = Optional.empty();

		if(authority.equals(Roles.ROLE_MANAGER.name()) || authority.equals(Roles.ROLE_HR.name())) {
			employees = empRepo.findByReportTo(loggedInUserEmailId);
			if(employees.isPresent()) {
				return new ResponseEntity<List<Employee>>(employees.get(), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Message>(new Message("No User present for manager :" + loggedInUserEmailId), HttpStatus.OK);
			}
		}
		else if(authority.equals(Roles.ROLE_USER.name())) {
			Optional<Employee> reportPerson = empRepo.findByEmail(loggedInUserEmailId);
			if(reportPerson.isPresent() && reportPerson.get().getManagerEmail() != null) {
				employees = empRepo.findByReportTo(reportPerson.get().getManagerEmail());
				if(employees.isPresent()) {
					return new ResponseEntity<List<Employee>>(employees.get(), HttpStatus.OK);
				}
				else {
					return new ResponseEntity<Message>(new Message("No User present for manager :" + loggedInUserEmailId), HttpStatus.OK);
				}
			}
		}
		else {
			employees = Optional.of(empRepo.findAll());
		}
		
		return new ResponseEntity<List<Employee>>(employees.get(), HttpStatus.OK);
	}

	
	@Override
	public ResponseEntity<?> createNewEmployee(@RequestBody SignupRequest signupRequest) throws Exception {
		
		String loggedInUserEmailId = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		
		Employee newEmployee = null;
		Set<Role> roles = new HashSet<>();
		
		if(signupRequest.getEmail() != null && signupRequest.getPassword() != null
				&& signupRequest.getFirstName() != null) {
			
			Optional<Employee> oldEmployee = empRepo.findByEmail(signupRequest.getEmail());
			
			if(oldEmployee.isPresent()) {
				return new ResponseEntity<Message>(new Message("User is already present with the email id : " + signupRequest.getEmail()), HttpStatus.BAD_REQUEST);
			}
			
			newEmployee = new Employee(signupRequest.getEmail(),
					 encoder.encode(signupRequest.getPassword()));
			
			EmployeeDetails newEmpDetails = new EmployeeDetails();
			newEmpDetails.setFirstName(signupRequest.getFirstName());
			newEmpDetails.setLastName(signupRequest.getLastName());
			
			newEmployee.setEmployeeDetails(newEmpDetails);
			
			if(authority.equals(Roles.ROLE_MANAGER.value)) {
				Role userRole = roleRepository.findByRole(Roles.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
				
				Optional<Employee> reportTo = empRepo.findByEmail(loggedInUserEmailId);
				
				newEmployee.setRoles(roles);
				newEmployee.setManagerEmail(reportTo.get().getEmail());
			}
			else if(authority.equals(Roles.ROLE_HR.value)) {
				if(signupRequest.getRole().size() > 0) {
					String role = signupRequest.getRole().toArray()[0].toString();
					Optional<Role> newUserrole = roleRepository.findByName(Roles.get(role).name());
					if(newUserrole.isPresent()) {
						roles.add(newUserrole.get());
						newEmployee.setRoles(roles);
					}
					if(role.equals(Roles.ROLE_MANAGER.name())) {
						Optional<Employee> reportTo = empRepo.findByEmail(loggedInUserEmailId);
						newEmployee.setManagerEmail(reportTo.get().getEmail());
					}
					if(role.equals(Roles.ROLE_USER.name())) {
						String managerId = signupRequest.getReportTo();
						if(managerId != null) {
							Optional<Employee> manager = empRepo.findByEmail(managerId);
							if(manager.isPresent()) {
								newEmployee.setManagerEmail(manager.get().getEmail());
							}
						}
					}
				}
				else {
					throw new NotFoundException("Role is not found");
				}
				
			}
			else {
				if(signupRequest.getRole().size() > 0) {
					String role = signupRequest.getRole().toArray()[0].toString();
					Optional<Role> newUserrole = roleRepository.findByName(Roles.get(role).name());
					if(newUserrole.isPresent()) {
						roles.add(newUserrole.get());
						newEmployee.setRoles(roles);
					}
					if(signupRequest.getReportTo() != null) {
						Optional<Employee> manager = empRepo.findByEmail(signupRequest.getReportTo());
						if(manager.isPresent()) {
							newEmployee.setManagerEmail(manager.get().getEmail());
						}
					}
				}
			}
			
			try {
				newEmployee = empRepo.save(newEmployee);
			}catch(IllegalArgumentException ex) {
				logger.debug("ERROR : On create a new employee with object : " + newEmployee.toString());
				logger.debug("ERROR : Error message is : " + ex.getMessage());
				throw new IllegalArgumentException("ERROR : On create a new employee with object : " + newEmployee.toString());
			}catch(Exception ex) {
				logger.debug("ERROR : On create a new employee with object : " + newEmployee.toString());
				logger.debug("ERROR : Error message is : " + ex.getMessage());
				throw new Exception("ERROR : On create a new employee with object : " + newEmployee.toString());
			}
		}
		else {
			if(signupRequest.getEmail() == null) {
				throw new NotFoundException("Email address is not found");
			}
			if(signupRequest.getPassword() == null) {
				throw new NotFoundException("Password address is not found");
			}
			if(signupRequest.getFirstName() == null) {
				throw new NotFoundException("First name is not found");
			}
		}
		
		
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> saveEmployeeDetails(EmployeeDetailsRequest employeeDetailsReq) throws Exception{
		Employee newEmployee = new Employee();
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		
		if(employeeDetailsReq != null && employeeDetailsReq.getId() != null 
				&& employeeDetailsReq.getEmployeeDetails().getFirstName() != null) {
			try {
				Optional<Employee> emp = empRepo.findById(Long.parseLong(employeeDetailsReq.getId()));
				if(emp.isPresent()) {
					Employee employee = emp.get();
					
					if(authority.equals(Roles.ROLE_USER.name())) {
						if(!loggedInUSerEmail.equals(employee.getEmail())) {
							logger.debug(loggedInUSerEmail = " this user is trying to save or update another user record.");
							return new ResponseEntity<Message>(new Message(loggedInUSerEmail = " this user is trying to save or update another user record."), HttpStatus.UNAUTHORIZED);
						}
					}
					if(authority.equals(Roles.ROLE_MANAGER.name())) {
						if(employee.getRoles().iterator().next().getRole().name().equals(Roles.ROLE_USER.name())) {
							if(!loggedInUSerEmail.equals(employee.getManagerEmail())) {
								logger.debug(loggedInUSerEmail = " this manager is trying to save or update another manager's employee record.");
								return new ResponseEntity<Message>(new Message(loggedInUSerEmail = " this manager is trying to save or update another manager's employee record."), HttpStatus.UNAUTHORIZED);
							}
						}
						else if(employee.getRoles().iterator().next().getRole().name().equals(Roles.ROLE_MANAGER.name())) {
							if(!loggedInUSerEmail.equals(employee.getEmail())) {
								logger.debug(loggedInUSerEmail = " this user is trying to save or update another user record.");
								return new ResponseEntity<Message>(new Message(loggedInUSerEmail = " this user is trying to save or update another user record."), HttpStatus.UNAUTHORIZED);
							}
						}
						
					}
					
					try {
						EmployeeDetails updatedEmpDetails = employee.getEmployeeDetails();
						updatedEmpDetails.setFirstName(employeeDetailsReq.getEmployeeDetails().getFirstName());
						updatedEmpDetails.setMiddleName(employeeDetailsReq.getEmployeeDetails().getMiddleName());
						updatedEmpDetails.setLastName(employeeDetailsReq.getEmployeeDetails().getLastName());
						updatedEmpDetails.setPhone(employeeDetailsReq.getEmployeeDetails().getPhone());
						updatedEmpDetails.setMobile(employeeDetailsReq.getEmployeeDetails().getMobile());
						updatedEmpDetails.setAddress(employeeDetailsReq.getEmployeeDetails().getAddress());
						updatedEmpDetails.setState(employeeDetailsReq.getEmployeeDetails().getState());
						updatedEmpDetails.setCity(employeeDetailsReq.getEmployeeDetails().getCity());
						updatedEmpDetails.setGender(employeeDetailsReq.getEmployeeDetails().getGender());
						updatedEmpDetails.setZip(employeeDetailsReq.getEmployeeDetails().getZip());
						updatedEmpDetails.setDateOfBirth(employeeDetailsReq.getEmployeeDetails().getDateOfBirth());
						updatedEmpDetails.setDateOfJoining(employeeDetailsReq.getEmployeeDetails().getDateOfJoining());
						
						if(employeeDetailsReq.getDesignation() != null) {
							try{
								Optional<Designation> empDesignation = designationRepository.findByDesignation(employeeDetailsReq.getDesignation());
								if(empDesignation.isPresent()) {
									updatedEmpDetails.setDesignation(empDesignation.get());
								}
								else {
									Designation newDesignation = designationRepository.save(new Designation(employeeDetailsReq.getDesignation()));
									if(newDesignation != null) {
										updatedEmpDetails.setDesignation(newDesignation);
									}
								}
							}catch(IllegalArgumentException ex) {
								throw new IllegalArgumentException("ERROR : While fetching existing or creating new designation from DB", ex);
							}
							
						}
						
						try {
							if(employeeDetailsReq.getStatus() != null) {
								Optional<EmployeeStatus> empStatus = empStatusRepository.findByStatus(employeeDetailsReq.getStatus());
								if(empStatus.isPresent()) {
									updatedEmpDetails.setEmpStatus(empStatus.get());
								}
								else {
									EmployeeStatus newEmpStatus = empStatusRepository.save(new EmployeeStatus(employeeDetailsReq.getStatus()));
									if(newEmpStatus != null) {
										updatedEmpDetails.setEmpStatus(newEmpStatus);
									}
								}
							}
						}catch(IllegalArgumentException ex) {
							throw new IllegalArgumentException("ERROR : While fetching existing or creating new employee status from DB", ex);
						}
						
						employee.setEmployeeDetails(updatedEmpDetails);
						newEmployee = empRepo.save(employee);
					}catch(Exception ex) {
						logger.debug("ERROR : Unalbe to save employee object");
						logger.debug("Error : Error message is : " + ex.getMessage());
						throw new Exception("ERROR : Unalbe to save employee object.", ex);
					}
					
					
				}
			}catch(NotFoundException ex) {
				logger.debug("ERROR : No Employye found for id : " + employeeDetailsReq.getId());
				logger.debug("Stack Trace : " + ex.getStackTrace());
				throw new NotFoundException("Employee not found for id :" + employeeDetailsReq.getId());
			}
		}
		else {
			if(employeeDetailsReq.getId() == null) {
				throw new NotFoundException("Employee Id is not found");
			}
			if(employeeDetailsReq.getEmployeeDetails().getFirstName() == null) {
				throw new NotFoundException("First name is not found");
			}
		}
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}
	
	
	
	@Override
	public ResponseEntity<?> getEmployeeById(String id) throws Exception{
		Optional<Employee> emp = Optional.empty();
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		
		try {
			if(id != null) {
				emp = empRepo.findById(Long.parseLong(id));
				if(emp.isPresent()) {
					if(authority.equals(Roles.ROLE_USER.name())) {
						if(!emp.get().getEmail().equals(loggedInUSerEmail)) {
							return new ResponseEntity<Message>(new Message("You don't have permission to view this user's details"), HttpStatus.UNAUTHORIZED);
						}
					}
					
					else if(authority.equals(Roles.ROLE_MANAGER.name()) || authority.equals(Roles.ROLE_HR.name())) {
						if(emp.get().getManagerEmail() != null) {
							if(!emp.get().getManagerEmail().equals(loggedInUSerEmail)) {
								return new ResponseEntity<Message>(new Message("You don't have permission to view this user's details"), HttpStatus.UNAUTHORIZED);
							}
						}
						else {
							if(!emp.get().getEmail().equals(loggedInUSerEmail)) {
								return new ResponseEntity<Message>(new Message("You don't have permission to view this user's details"), HttpStatus.UNAUTHORIZED);
							}
						}
						
						if(authority.equals(Roles.ROLE_HR.name()) && 
								emp.get().getRoles().iterator().next().getRole().name().equals(Roles.ROLE_USER.name())) {
							String managerOfUser = emp.get().getManagerEmail();
							Optional<Employee> manager = empRepo.findByEmail(managerOfUser);
							if(manager.isPresent()) {
								if(manager.get().getManagerEmail() != null &&
										!manager.get().getManagerEmail().equals(loggedInUSerEmail)) {
									return new ResponseEntity<Message>(new Message("You don't have permission to view this user's details"), HttpStatus.UNAUTHORIZED);
								}
							}
						}
					}
				}
				else {
					return new ResponseEntity<Message>(new Message("User not found for id : " + id), HttpStatus.NOT_FOUND);
				}
				
			}
			else {
				return new ResponseEntity<Message>(new Message("User Id should not be null"), HttpStatus.BAD_REQUEST);
			}
		}catch(EntityNotFoundException ex) {
			logger.debug("ERROR : No Employye found for id : " + id);
			logger.debug("Stack Trace : " + ex.getStackTrace());
			throw new EntityNotFoundException("ERROR : No Employye found for id : " + id);
		}
		catch(Exception ex) {
			logger.debug("ERROR : Error message : " + ex.getMessage());
			logger.debug("ERROR : Stack Trace : " + ex.getStackTrace());
			throw new Exception(ex);
		}
		
		return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> deleteEmployeeById(String id) throws Exception{
		Optional<Employee> emp = Optional.empty();
		String loggedInUSerEmail = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		
		try {
			if(id != null) {
				emp = empRepo.findById(Long.parseLong(id));
				if(emp.isPresent()) {
					if(authority.equals(Roles.ROLE_HR.name())) {
						
						if(emp.get().getRoles().iterator().next().getRole().name().equals(Roles.ROLE_USER.name())) {
							String managerOfUser = emp.get().getManagerEmail();
							Optional<Employee> manager = empRepo.findByEmail(managerOfUser);
							if(manager.isPresent()) {
								if(manager.get().getManagerEmail() != null &&
										!manager.get().getManagerEmail().equals(loggedInUSerEmail)) {
									return new ResponseEntity<Message>(new Message("You don't have permission to delete this user."), HttpStatus.UNAUTHORIZED);
								}
							}
						}
						else if(emp.get().getRoles().iterator().next().getRole().name().equals(Roles.ROLE_MANAGER.name())) {
							if(!emp.get().getManagerEmail().equals(loggedInUSerEmail)) {
								return new ResponseEntity<Message>(new Message("You don't have permission to delete this user."), HttpStatus.UNAUTHORIZED);
							}
							else {
								Optional<List<Employee>> users = empRepo.findByReportTo(emp.get().getEmail());
								if(users.isPresent() && users.get().size() > 0) {
									return new ResponseEntity<Message>(new Message("You can't delete this employee. There are many employees under this employee."), HttpStatus.UNAUTHORIZED);
								}
							}
						}
						else if(emp.get().getRoles().iterator().next().getRole().name().equals(Roles.ROLE_HR.name())) {
							return new ResponseEntity<Message>(new Message("You don't have permission to delete this user."), HttpStatus.UNAUTHORIZED);
						}
						
					}
					
					if(authority.equals(Roles.ROLE_ADMIN.name())) {
						
						if(emp.get().getRoles().iterator().next().getRole().name().equals(Roles.ROLE_MANAGER.name()) ||
								emp.get().getRoles().iterator().next().getRole().name().equals(Roles.ROLE_HR.name())) {
							Optional<List<Employee>> users = empRepo.findByReportTo(emp.get().getEmail());
							if(users.isPresent() && users.get().size() > 0) {
								return new ResponseEntity<Message>(new Message("You can't delete this employee. There are many employees under this employee."), HttpStatus.UNAUTHORIZED);
							}
						}
					}
					
					if(emp.get().getEmail().equals(loggedInUSerEmail)) {
						return new ResponseEntity<Message>(new Message("You can't delete yourself."), HttpStatus.UNAUTHORIZED);
					}
					
					
					empRepo.deleteById(Long.parseLong(id));
				}
				else {
					return new ResponseEntity<Message>(new Message("No user found for id : "+ id), HttpStatus.NOT_FOUND);
				}
			}
			
		}catch(IllegalArgumentException ex) {
			logger.debug("Illigal argument exception on deleting employee with id : " + id + ", " +ex.getMessage());
			throw new IllegalArgumentException("Illigal argument exception on deleting employee with id : " + id + ", " +ex.getMessage());
		}
		catch(Exception ex) {
			logger.debug("ERROR : Exception on delete employee : " + ex.getMessage());
			logger.debug("Exception : " + ex.getStackTrace());
			throw new Exception(ex);
			
		}
		return new ResponseEntity<Message>(new Message("Employee deleted successfully with id : " + id), HttpStatus.OK);
	}
	
	
	
}
