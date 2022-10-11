package com.viniciusvieira.questionsanswers.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface CustomRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
	
	@Override
	@Query(value = "SELECT * FROM #{#entityName} e WHERE e.id_entity = :id AND e.professor = #{principal.professor} "
			+ "AND e.enabled = true", nativeQuery = true)
	Optional<T> findById(@Param("id")ID id);

	@Override 
	default boolean existsById(ID id) {
		return findById(id) != null;
	}

	// TEST
	@Override
	@Query(value = "SELECT * FROM #{#entityName} e WHERE e.professor = #{principal.professor} "
			+ "AND e.enabled = true", nativeQuery = true)
	Iterable<T> findAll();

	// TEST
	@Override
	@Query(value = "SELECT * FROM #{#entityName} e WHERE e.professor = #{principal.professor} "
			+ "AND e.enabled = true", nativeQuery = true)
	Iterable<T> findAllById(Iterable<ID> ids);

	@Override
	@Query(value = "SELECT count(e.id_entity) FROM #{#entityName} e WHERE e.professor = #{principal.professor} "
			+ "AND e.enabled = true", nativeQuery = true)
	long count();

	@Override
	@Transactional
	@Modifying
	@Query(value = "UPDATE #{#entityName} e SET e.enabled = false WHERE e.id_entity = :id "
			+ "AND e.professor = #{principal.professor}", nativeQuery = true)
	void deleteById(@Param("id")ID id);

//	@Override
//	@Transactional
//	@Modifying
//	default void delete(T entity) {
//		delete(entity.getIdEntity());
//	}

//	@Override
//	@Transactional
//	@Modifying
//	default void deleteAllById(Iterable<? extends ID> ids) {
//		ids.forEach(entitys -> delete(entitys.getIdEntity()));
//	}


	@Override
	@Transactional
	@Modifying
	@Query("UPDATE #{entityName} e SET e.enabled = false WHERE e.professor = {#principal.professor}")
	void deleteAll();

	@Override
	@Query(value = "SELECT * FROM #{#entityName} e WHERE e.professor = #{principal.professor} AND e.enabled = true")
	Iterable<T> findAll(Sort sort);

	@Override
	@Query(value = "SELECT * FROM #{#entityName} e WHERE e.professor = #{principal.professor} AND e.enabled = true")
	Page<T> findAll(Pageable pageable);
	
}
