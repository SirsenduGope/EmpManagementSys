package com.management.employee.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.employee.entity.Employee;
import com.management.employee.entity.EmployeeDetails;
import com.management.employee.entity.Role;
import com.management.employee.entity.Views;
import com.management.employee.enums.Roles;
import com.management.employee.payload.Message;
import com.management.employee.payload.SignupRequest;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.repository.RoleRepository;
import com.management.employee.utils.Helper;


@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
	
	private EmployeeRepository empRepo;
	private PasswordEncoder encoder;
	private RoleRepository roleRepository;
	
	public EmployeeController(final EmployeeRepository empRepo,
			final PasswordEncoder encoder,
			final RoleRepository roleRepository) {
		this.empRepo = empRepo;
		this.encoder = encoder;
		this.roleRepository = roleRepository;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	
//	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
//	@RequestMapping(value = "/saveemployeedetails", method = RequestMethod.POST)
//	public ResponseEntity<?> saveEmployeeDetails(@RequestBody EmployeeDetails employeeDetails){
//		Employee newEmployee = new Employee();
//		if(employeeDetails != null && employeeDetails.getFirstName() != null) {
//			String loginUserId = currentEmployee.getEmail();
//			if(loginUserId != null) {
//				try {
//					Employee emp = empRepo.findByEmail(loginUserId);
//					emp.setEmployeeDetails(employeeDetails);
//					newEmployee = empRepo.save(emp);
//				}catch(Exception ex) {
//					logger.debug("ERROR : No Employye found for email : " + loginUserId);
//					logger.debug("Stack Trace : " + ex.getStackTrace());
//				}
//			}
//			else {
//				return new ResponseEntity<Message>(new Message("Unable to get the current login user"), HttpStatus.NOT_FOUND);
//			}
//		}
//		return new ResponseEntity<Employee>(newEmployee, HttpStatus.CREATED);
//	}
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> createNewEmployee(@RequestBody SignupRequest signupRequest) {
		
		String loggedInUserEmailId = Helper.loggedInUserEmailId();
		String authority = Helper.loggedInUserAuthority();
		
		Employee newEmployee = null;
		Set<Role> roles = new HashSet<>();
		
		if(signupRequest.getEmail() != null && signupRequest.getPassword() != null
				&& signupRequest.getFirstName() != null) {
			
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
				
				Optional<Employee> reportTo = empRepo.findByEmail(loggedInUserEmailId);
				
				newEmployee.setRoles(roles);
				newEmployee.setManager(reportTo.get());
			}
			else if(authority.equals(Roles.ROLE_HR.name())) {
				String role = signupRequest.getRole().toArray()[0].toString();
				Optional<Role> newUserrole = roleRepository.findByName(role);
				if(newUserrole.isPresent()) {
					roles.add(newUserrole.get());
					newEmployee.setRoles(roles);
				}
				if(role.equals(Roles.ROLE_MANAGER.name())) {
					Optional<Employee> reportTo = empRepo.findByEmail(loggedInUserEmailId);
					newEmployee.setManager(reportTo.get());
				}
				if(role.equals(Roles.ROLE_USER.name())) {
					String managerId = signupRequest.getReportTo();
					if(managerId != null) {
						Optional<Employee> manager = empRepo.findByEmail(managerId);
						if(manager.isPresent()) {
							newEmployee.setManager(manager.get());
						}
					}
				}
			}
			else {
				//NEED TO DO FOR ADMIN
			}
		}
		
		
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/allemployee", method = RequestMethod.GET)
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
			if(reportPerson.isPresent() && reportPerson.get().getManager() != null && reportPerson.get().getManager().getEmail() != null) {
				employees = empRepo.findByReportTo(reportPerson.get().getManager().getEmail());
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
	
//	@PreAuthorize("hasRole('USER') or hasRole('HR') or hasRole('ADMIN') or hasRole('MANAGER')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
		Employee emp = new Employee();
		try {
			if(id != null) {
				emp = empRepo.getById(Long.parseLong(id));
				if(emp == null) {
					return new ResponseEntity<Message>(new Message("Employee not found for Id : " + id), HttpStatus.NOT_FOUND);
				}
			}
		}catch(EntityNotFoundException ex) {
			logger.debug("ERROR : No Employye found for id : " + id);
			logger.debug("Stack Trace : " + ex.getStackTrace());
		}
		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id){
		try {
			if(id != null) {
				empRepo.deleteById(Long.parseLong(id));
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("Illigal argument exception on deleting employee with id : " + id + ", " +ex.getMessage());
		}
		catch(Exception ex) {
			logger.debug("ERROR : Exception on delete employee : " + ex.getMessage());
			logger.debug("Exception : " + ex.getStackTrace());
		}
		return new ResponseEntity<Message>(new Message("Employee deleted successfully with id : " + id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee){
		Employee updatedEmployee = new Employee();
		try {
			if(employee.getId() != null && employee.getEmail() != null) {
				updatedEmployee = empRepo.save(employee);
			}
		}catch(IllegalArgumentException ex) {
			logger.debug("Illigal argument exception on updating employee, "+ex.getMessage());
		}
		catch(Exception ex) {
			logger.debug("ERROR : Exception on updating employee : " + ex.getMessage());
			logger.debug("Exception : " + ex.getStackTrace());
		}
		
		return new ResponseEntity<Employee>(updatedEmployee, HttpStatus.OK);
	}
	
}
