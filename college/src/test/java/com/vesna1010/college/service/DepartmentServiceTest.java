package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.vesna1010.college.models.Department;
import com.vesna1010.college.repositories.DepartmentRepository;
import com.vesna1010.college.services.DepartmentService;

public class DepartmentServiceTest extends BaseServiceTest {

	@Autowired
	private DepartmentService service;
	@MockBean
	private DepartmentRepository repository;

	@Test
	public void findAllDepartmentsWithSortTest() {
		when(repository.findAll(SORT))
				.thenReturn(Arrays.asList(new Department(1L, "Department A"), new Department(2L, "Department B")));

		List<Department> departments = service.findAllDepartments(SORT);

		assertThat(departments, hasSize(2));
		assertThat(departments.get(0).getName(), is("Department A"));
		assertThat(departments.get(1).getName(), is("Department B"));
		verify(repository, times(1)).findAll(SORT);
	}

	@Test
	public void findAllDepartmentsWithPageableTest() {
		when(repository.findAll(PAGEABLE)).thenReturn(new PageImpl<Department>(
				Arrays.asList(new Department(2L, "Department A"), new Department(2L, "Department B"))));

		Page<Department> page = service.findAllDepartments(PAGEABLE);
		List<Department> departments = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(departments, hasSize(2));
		assertThat(departments.get(0).getName(), is("Department A"));
		assertThat(departments.get(1).getName(), is("Department B"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findDepartmentByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(new Department(1L, "Department")));

		Department department = service.findDepartmentById(1L);

		assertThat(department.getName(), is("Department"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findDepartmentByIdNotFoundTest() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		service.findDepartmentById(1L);
	}

	@Test
	public void saveDepartmentTest() {
		Department department = new Department(1L, "Department");

		when(repository.save(department)).thenReturn(department);

		Department departmentSaved = service.saveDepartment(department);

		assertThat(departmentSaved.getId(), is(1L));
		verify(repository, times(1)).save(department);
	}

	@Test
	public void deleteDepartmentByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteDepartmentById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}
