package com.management.employee.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
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
import com.management.employee.enums.Gender;
import com.management.employee.enums.Roles;
import com.management.employee.payload.EmployeeDetailsRequest;
import com.management.employee.payload.Message;
import com.management.employee.payload.SignupRequest;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.repository.RoleRepository;
import com.management.employee.service.IDesignationService;
import com.management.employee.service.IEmployeeService;
import com.management.employee.service.IEmployeeStatusService;
import com.management.employee.service.ILeaveService;
import com.management.employee.utils.Helper;

import javassist.NotFoundException;

@Service
public class EmployeeServiceImpl implements IEmployeeService{

	private PasswordEncoder encoder;
	
	private EmployeeRepository empRepo;
	private RoleRepository roleRepository;
	
	private ILeaveService leaveService;
	private IDesignationService designationService;
	private IEmployeeStatusService employeeStatusService;
	
	public EmployeeServiceImpl(final EmployeeRepository empRepo,
			final PasswordEncoder encoder,
			final IDesignationService designationService,
			final IEmployeeStatusService employeeStatusService,
			final RoleRepository roleRepository,
			@Lazy final ILeaveService leaveService) {
		this.empRepo = empRepo;
		this.encoder = encoder;
		this.designationService = designationService;
		this.employeeStatusService = employeeStatusService;
		this.roleRepository = roleRepository;
		this.leaveService = leaveService;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	
	@Override
	public ResponseEntity<?> getAllEmployee() throws Exception {
		String loggedInUserEmailId = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		Optional<List<Employee>> employees = Optional.empty();
		try {
			if(authority.equals(Roles.ROLE_MANAGER.name())) {
				employees = getAllEmployeesUnderLoggedInUser();
				if(employees.isPresent()) {
					return new ResponseEntity<List<Employee>>(employees.get(), HttpStatus.OK);
				}
				else {
					return new ResponseEntity<Message>(new Message("No User present for manager :" + loggedInUserEmailId), HttpStatus.OK);
				}
			}
			else if(authority.equals(Roles.ROLE_HR.name())) {
				employees = getAllEmployeesUnderLoggedInUser();
				if(employees.isPresent()) {
					List<Employee> users = new ArrayList<Employee>();
					for(Employee manager : employees.get()) {
						Optional<List<Employee>> usersUnderManger = getAllEmployeesUnderReportToUser(manager.getEmail());
						if(usersUnderManger.isPresent()) {
							users.addAll(usersUnderManger.get());
						}
					}
					if(users.size() > 0) {
						employees.get().addAll(users);
					}
				}
			}
			else if(authority.equals(Roles.ROLE_USER.name())) {
				Optional<Employee> reportPerson = getLoggedInEmployeeDetails();
				if(reportPerson.isPresent() && reportPerson.get().getManagerEmail() != null) {
					employees = getAllEmployeesUnderReportToUser(reportPerson.get().getManagerEmail());
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
		}catch(NotFoundException ex) {
			logger.debug("ERROR : No Employye found for user : " + loggedInUserEmailId);
			logger.debug("Stack Trace : " + ex.getStackTrace());
			throw new NotFoundException("No Employye found for user : " + loggedInUserEmailId);
		}catch(Exception ex) {
			logger.debug("ERROR : On getting all employee from method getAllEmployee");
			logger.debug("Stack Trace : " + ex.getStackTrace());
			throw new Exception("No Employye found for user : " + loggedInUserEmailId);
		}
		
		
		return new ResponseEntity<List<Employee>>(employees.get(), HttpStatus.OK);
	}

	
	@Transactional
	@Override
	public ResponseEntity<?> createNewEmployee(@RequestBody SignupRequest signupRequest) throws Exception {
		String loggedInUserEmail = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		
		Employee newEmployee = null;
		Set<Role> roles = new HashSet<>();
		
		if(signupRequest.getEmail() != null && signupRequest.getPassword() != null
				&& signupRequest.getFirstName() != null) {
			
			Optional<Employee> oldEmployee = getEmployeeByEmail(signupRequest.getEmail());
			
			if(oldEmployee.isPresent()) {
				return new ResponseEntity<Message>(new Message("User is already present with the email id : " + signupRequest.getEmail()), HttpStatus.BAD_REQUEST);
			}
			
			newEmployee = new Employee(signupRequest.getEmail(),
					 encoder.encode(signupRequest.getPassword()));
			
			EmployeeDetails newEmpDetails = new EmployeeDetails();
			newEmpDetails.setFirstName(signupRequest.getFirstName());
			newEmpDetails.setLastName(signupRequest.getLastName());
			
			newEmployee.setEmployeeDetails(newEmpDetails);
			
			if(authority.equals(Roles.ROLE_MANAGER.name())) {
				Role userRole = roleRepository.findByRole(Roles.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
				
				newEmployee.setRoles(roles);
				newEmployee.setManagerEmail(loggedInUserEmail);
			}
			else if(authority.equals(Roles.ROLE_HR.name())) {
				if(signupRequest.getRole().size() > 0) {
					String role = signupRequest.getRole().toArray()[0].toString();
					Optional<Role> newUserrole = roleRepository.findByName(Roles.get(role).name());
					if(newUserrole.isPresent()) {
						roles.add(newUserrole.get());
						newEmployee.setRoles(roles);
					}
					if(role.equals(Roles.ROLE_MANAGER.value)) {
						newEmployee.setManagerEmail(loggedInUserEmail);
					}
					if(role.equals(Roles.ROLE_USER.value)) {
						String managerId = signupRequest.getReportTo();
						if(managerId != null) {
							Optional<Employee> manager = getEmployeeByEmail(managerId);
							if(manager.isPresent()) {
								newEmployee.setManagerEmail(manager.get().getEmail());
							}
							else {
								return new ResponseEntity<Message>(new Message("No reporting user is found with email id : " + signupRequest.getReportTo()), HttpStatus.BAD_REQUEST);
							}
						}else {
							return new ResponseEntity<Message>(new Message("No reporting user is found with email id : " + signupRequest.getReportTo()), HttpStatus.BAD_REQUEST);
						}
					}
				}
				else {
					return new ResponseEntity<Message>(new Message("Role is not found"), HttpStatus.BAD_REQUEST);
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
						Optional<Employee> manager = getEmployeeByEmail(signupRequest.getReportTo());
						if(manager.isPresent()) {
							newEmployee.setManagerEmail(manager.get().getEmail());
						}
					}
					else {
						newEmployee.setManagerEmail(loggedInUserEmail);
					}
				}
			}
			
			try {
				newEmployee = empRepo.saveAndFlush(newEmployee);
				
				ResponseEntity<?> response = leaveService.generateLeaveCountDetailsForEmployee(newEmployee);
				if(response.getStatusCodeValue() < 200 || response.getStatusCodeValue() > 299) {
					logger.debug("ERROR : Error occored on setting leave details for employee : " + newEmployee.toString());
					logger.debug("ERROR : " + response.getBody().toString());
					throw new Exception("ERROR : Error occored on setting leave details for employee : " + newEmployee.toString());
				}
				
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
	
	
	@Transactional
	@Override
	public ResponseEntity<?> saveEmployeeDetails(EmployeeDetailsRequest employeeDetailsReq) throws Exception{
		Employee newEmployee = new Employee();
		boolean hasAccess = false;
		
		if(employeeDetailsReq != null && employeeDetailsReq.getId() != null 
				&& employeeDetailsReq.getFirstName() != null) {
			try {
				
				hasAccess = isLoggedInUserHasAccessToThisEmployee(Long.parseLong(employeeDetailsReq.getId()), true);
				if(!hasAccess) {
					return new ResponseEntity<Message>(new Message("You don't have access save or update another user record."), HttpStatus.FORBIDDEN);
				}
				Optional<Employee> emp = getEmployeeById(Long.parseLong(employeeDetailsReq.getId()));
				if(emp.isPresent()) {
					Employee employee = emp.get();
					
					
					try {
						EmployeeDetails updatedEmpDetails = employee.getEmployeeDetails();
						updatedEmpDetails.setFirstName(employeeDetailsReq.getFirstName());
						updatedEmpDetails.setMiddleName(employeeDetailsReq.getMiddleName());
						updatedEmpDetails.setLastName(employeeDetailsReq.getLastName());
						updatedEmpDetails.setPhone(employeeDetailsReq.getPhone());
						updatedEmpDetails.setMobile(employeeDetailsReq.getMobile());
						updatedEmpDetails.setAddress(employeeDetailsReq.getAddress());
						updatedEmpDetails.setState(employeeDetailsReq.getState());
						updatedEmpDetails.setCity(employeeDetailsReq.getCity());
						updatedEmpDetails.setZip(employeeDetailsReq.getZip());
						updatedEmpDetails.setDateOfBirth(employeeDetailsReq.getDateOfBirth());
						updatedEmpDetails.setDateOfJoining(employeeDetailsReq.getDateOfJoining());
						
						if(employeeDetailsReq.getGender().toString().equals(Gender.MALE.name())) {
							updatedEmpDetails.setGender(Gender.MALE);
						}
						else if(employeeDetailsReq.getGender().toString().equals(Gender.FEMALE.name())) {
							updatedEmpDetails.setGender(Gender.FEMALE);
						}
						else {
							updatedEmpDetails.setGender(Gender.OTHERS);
						}
						
						
						if(employeeDetailsReq.getDesignation() != null) {
							try{
								Optional<Designation> empDesignation = designationService.getDesignationDetailsByName(employeeDetailsReq.getDesignation());
								if(empDesignation.isPresent()) {
									updatedEmpDetails.setDesignation(empDesignation.get());
								}
								else {
									ResponseEntity<?> newDesignation = designationService.addNewDesignation(new Designation(employeeDetailsReq.getDesignation()));
									if(newDesignation.getStatusCodeValue() >= 200 && newDesignation.getStatusCodeValue() < 300) {
										updatedEmpDetails.setDesignation((Designation) newDesignation.getBody());
									}
								}
							}catch(IllegalArgumentException ex) {
								throw new IllegalArgumentException("ERROR : While fetching existing or creating new designation from DB", ex);
							}
							
						}
						
						try {
							if(employeeDetailsReq.getStatus() != null) {
								Optional<EmployeeStatus> empStatus = employeeStatusService.getEmployeeStatusByName(employeeDetailsReq.getStatus());
								if(empStatus.isPresent()) {
									updatedEmpDetails.setEmpStatus(empStatus.get());
								}
								else {
									ResponseEntity<?> newEmpStatus = employeeStatusService.addNewEmployeeStauts(new EmployeeStatus(employeeDetailsReq.getStatus()));
									if(newEmpStatus.getStatusCodeValue() >=200 || newEmpStatus.getStatusCodeValue() < 300) {
										updatedEmpDetails.setEmpStatus((EmployeeStatus) newEmpStatus.getBody());
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
			if(employeeDetailsReq.getFirstName() == null) {
				throw new NotFoundException("First name is not found");
			}
		}
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}
	
	
	
	@Override
	public ResponseEntity<?> getEmployeeById(String id) throws Exception{
		Optional<Employee> emp = Optional.empty();
		boolean hasAccess = false;
		
		try {
			if(id != null) {
				hasAccess = isLoggedInUserHasAccessToThisEmployee(Long.parseLong(id), true);
				if(!hasAccess) {
					return new ResponseEntity<Message>(new Message("You don't have access to get another user's details."), HttpStatus.FORBIDDEN);
				}
				emp = getEmployeeById(Long.parseLong(id));
				if(emp.isEmpty()) {
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
	
	
	@Transactional
	@Override
	public ResponseEntity<?> deleteEmployeeById(String id) throws Exception{
		Optional<Employee> emp = Optional.empty();
		boolean hasAccess = false;
		
		try {
			if(id != null) {
				hasAccess = isLoggedInUserHasAccessToThisEmployee(Long.parseLong(id), false);
				if(!hasAccess) {
					return new ResponseEntity<Message>(new Message("You don't have access to delete this user."), HttpStatus.FORBIDDEN);
				}
				emp = getEmployeeById(Long.parseLong(id));
				if(emp.isPresent()) {
					Optional<List<Employee>> users = getAllEmployeesUnderReportToUser(emp.get().getEmail());
					if(users.isPresent() && users.get().size() > 0) {
						return new ResponseEntity<Message>(new Message("You can't delete this employee. There are many employees under this employee."), HttpStatus.UNAUTHORIZED);
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
	
	
	
	@Override
	public ResponseEntity<?> getEmployeesByRole(String role) throws Exception{
		Optional<List<Employee>> employees = Optional.empty();
		String authority = Helper.loggedInUserAuthority();
		
		try {
			if(role != null) {
				if(authority.equals(Roles.ROLE_HR.name())) {
					//Getting all the users under an HR
					if(role.equals(Roles.ROLE_USER.value)) {
						try {
							Optional<List<Employee>> managers = getAllEmployeesUnderLoggedInUser();
							if(managers.isPresent()) {
								List<Employee> users = new ArrayList<Employee>();
								for(Employee manager : managers.get()) {
									Optional<List<Employee>> usersUnderManger = getAllEmployeesUnderReportToUser(manager.getEmail());
									if(usersUnderManger.isPresent()) {
										users.addAll(usersUnderManger.get());
									}
								}
								employees = Optional.of(users);
							}
						}catch(Exception ex) {
							throw new Exception("ERROR : Error on getting users." + ex.getMessage());
						}
					}
					//Getting all the managers under an HR
					else if(role.equals(Roles.ROLE_MANAGER.value)) {
						try {
							Optional<List<Employee>> managers = Optional.empty();
							managers = getAllEmployeesUnderLoggedInUser();
							if(managers.isPresent()) {
								employees = managers;
							}
						}catch(Exception ex) {
							throw new Exception("ERROR : Error on getting users." + ex.getMessage());
						}
					}
					else if(role.equals(Roles.ROLE_HR.value) || role.equals(Roles.ROLE_ADMIN.value)) {
						return new ResponseEntity<Message>(new Message("To get this list of users, you need to login as Admin"), HttpStatus.BAD_REQUEST); 
					}
				}
				if(authority.equals(Roles.ROLE_ADMIN.name())) {
					try {
						Optional<Role> existingRole = roleRepository.findByName(Roles.get(role).name());
						if(existingRole.isPresent()) {
							employees = empRepo.findByRoleId(existingRole.get().getId());
						}
					}catch(Exception ex) {
						throw new Exception("ERROR : Error on getting users." + ex.getMessage());
					}
				}
			}
			else {
				return new ResponseEntity<Message>(new Message("Role should not be null"), HttpStatus.BAD_REQUEST);
			}
		}catch(EntityNotFoundException ex) {
			logger.debug("ERROR : No Employyes found for role : [" + role + "]");
			logger.debug("Stack Trace : " + ex.getStackTrace());
			throw new EntityNotFoundException("ERROR : No Employyes found for role : [" + role + "]");
		}
		catch(Exception ex) {
			logger.debug("ERROR : Error message : " + ex.getMessage());
			logger.debug("ERROR : Stack Trace : " + ex.getStackTrace());
			throw new Exception(ex);
		}
		
		return new ResponseEntity<List<Employee>>(employees.get(), HttpStatus.OK);
	}
	
	
	@Override
	public boolean isLoggedInUserHasAccessToThisEmployee(Long empId, boolean selfAccess) throws Exception {
		String loggedInEmpRole = Helper.loggedInUserAuthority();
		String loggedInEmpEmail = Helper.loggedInUserEmailId();
		
		if(empId != null) {
			Optional<Employee> emp = getEmployeeById(empId);
			if(emp.isEmpty()) {
				logger.debug("User not found for id : " + empId);
			}
			
			Employee requestedEmp = emp.get();
			
			if(loggedInEmpEmail.equals(requestedEmp.getEmail())) {
				return selfAccess;
			}
			else {
				if(loggedInEmpRole.equals(Roles.ROLE_MANAGER.name())) {
					//In this position, the requested employee must be an USER role
					if(!requestedEmp.getManagerEmail().equals(loggedInEmpEmail)) {
						return false;
					}
				}
				if(loggedInEmpRole.equals(Roles.ROLE_HR.name())) {
					//In this position, the requested employee must be an MANAGER role
					if(!requestedEmp.getManagerEmail().equals(loggedInEmpEmail)) {
						//In this position, the requested employee must be an USER role
						Employee employee = getEmployeeByEmail(requestedEmp.getManagerEmail()).get();
						if(!employee.getManagerEmail().equals(loggedInEmpEmail)) {
							return false;
						}
					}
				}
				if(loggedInEmpRole.equals(Roles.ROLE_USER.name())) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public Optional<Employee> getLoggedInEmployeeDetails() throws Exception{
		String loggedInUserEmail = Helper.loggedInUserEmailId();
		try {
			return empRepo.findByEmail(loggedInUserEmail);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getEmployeeByEmail method.");
			throw new Exception(ex);
		}
	}
	
	@Override
	public Optional<List<Employee>> getAllEmployeesUnderLoggedInUser() throws Exception {
		String loggedInUserEmail = Helper.loggedInUserEmailId();
		try {
			return empRepo.findByReportTo(loggedInUserEmail);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getAllEmployeeUnderLoggedInUser method.");
			throw new Exception(ex);
		}
	}
	
	@Override
	public Optional<List<Long>> getAllEmployeesIdsUnderLoggedInUser() throws Exception {
		String loggedInUserEmail = Helper.loggedInUserEmailId();
		try {
			return empRepo.findAllEmployeeIdByReportTo(loggedInUserEmail);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getAllEmployeeIdsUnderLoggedInUser method.");
			throw new Exception(ex);
		}
	}
	
	@Override
	public Optional<List<Long>> getAllEmployeesIdUnderReportToUser(String reportToUserEmail) throws Exception {
		try {
			return empRepo.findAllEmployeeIdByReportTo(reportToUserEmail);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getAllEmployeeIdUnderReportToUser method.");
			throw new Exception(ex);
		}
	}
	
	
	@Override
	public Optional<List<Employee>> getAllEmployeesUnderReportToUser(String reportToUserEmail) throws Exception {
		try {
			return empRepo.findByReportTo(reportToUserEmail);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getAllEmployeeIdUnderReportToUser method.");
			throw new Exception(ex);
		}
	}
	
	@Override
	public Optional<Employee> getEmployeeById(Long id) throws Exception{
		try {
			return empRepo.findById(id);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getEmployeeById method.");
			throw new Exception(ex);
		}
	}
	
	@Override
	public Optional<Employee> getEmployeeByEmail(String email) throws Exception{
		try {
			return empRepo.findByEmail(email);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getEmployeeByEmail method.");
			throw new Exception(ex);
		}
	}
	
	@Override
	public List<Employee> getAllEmployeesByIds(List<Long> ids) throws Exception{
		try {
			return empRepo.findAllById(ids);
		}catch(Exception ex) {
			logger.debug("Error : Exception occor from getEmployeeByEmail method.");
			throw new Exception(ex);
		}
	}
	
}
