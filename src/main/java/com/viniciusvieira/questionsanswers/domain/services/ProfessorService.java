package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v2.ApplicationUserMapper;
import com.viniciusvieira.questionsanswers.api.mappers.v2.ProfessorMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
	private final ProfessorRepository professorRepository;
	private final ProfessorMapper professorMapper;
	private final ApplicationUserMapper applicationUserMapper;
	private final InsertUserService insertUserService;
	private final DeleteUserService deleteUserService;
	
	public ProfessorModel findByIdOrThrowProfessorNotFoundException(Long id) {
		return professorRepository.findById(id)
				.orElseThrow(() -> new ProfessorNotFoundException("Professor Not Found"));
	}

	public List<ProfessorDto> findByName(String name){
		List<ProfessorModel> professors = professorRepository.findByNameContaining(name);

		if (professors.isEmpty()){
			throw new ProfessorNotFoundException("Professor List is empty");
		}

		return professorMapper.toProfessorDtoList(professors);
	}

	@Transactional
	public ProfessorDto saveProfessor(ProfessorRequestBody professorRequestBody) {
		ProfessorModel professor = professorMapper.toProfessorDomain(professorRequestBody);

		ProfessorModel professorSaved = professorRepository.save(professor);
		return professorMapper.toProfessorDto(professorSaved);
	}

	@Transactional
	public ApplicationUserDto saveApplicationUserProfessor(Long idProfessor, ApplicationUserRequestBody
			applicationUserRequestBody) {
		ProfessorModel professorFound = findByIdOrThrowProfessorNotFoundException(idProfessor);

		ApplicationUserModel applicationUserSaved = insertUserService.insertProfessor(professorFound,
				applicationUserRequestBody);

		return applicationUserMapper.toApplicationUserDto(applicationUserSaved);
	}

	@Transactional
	public ProfessorDto replace(Long idProfessor, ProfessorRequestBody professorRequestBody) {
		ProfessorModel professor = professorMapper.toProfessorDomain(professorRequestBody);
		ProfessorModel professorFound = findByIdOrThrowProfessorNotFoundException(idProfessor);

		professorFound.setEmail(professor.getEmail());
		professorFound.setName(professor.getName());
		ProfessorModel professorReplaced = professorRepository.save(professorFound);

		return professorMapper.toProfessorDto(professorReplaced);
	}

	@Transactional
	public void delete(Long idProfessor) {
		ProfessorModel professorFound = findByIdOrThrowProfessorNotFoundException(idProfessor);

		deleteUserService.deleteProfessor(professorFound);
		professorRepository.deleteById(professorFound.getIdProfessor());
	}
}
