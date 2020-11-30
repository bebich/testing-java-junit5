package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void deleteById() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeastOnce() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);
    }

    @Test
    void deleteByIdAtMostOnce() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(atMost(2)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);
        then(specialtyRepository).should(never()).deleteById(5L);
    }
    @Test
    void testDelete(){
        //given
        Specialty specialty = new Specialty();

        //when
        service.delete(specialty);

        //then
        then(specialtyRepository).should().delete(any(Specialty.class));
    }

    @Test
    void TestDeleteByObject() {
        //given
        Specialty specialty = new Specialty();

        //when
        service.delete(specialty);

        //then
        then(specialtyRepository).should().delete(any(Specialty.class));
    }
    @Test
    void findByIdTest() {
        //given
        Specialty specialty = new Specialty();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(specialty));

        //when
        Specialty foundSpecialty = service.findById(1L);

        //then
        assertThat(foundSpecialty).isNotNull();
        then(specialtyRepository).should(timeout(100).times(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("BOOOOOM")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> service.delete(new Specialty()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("BOOM"));

        assertThrows(RuntimeException.class, () -> service.findById(1L));

        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("BOOM")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> service.delete(new Specialty()));

        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Specialty specialty = new Specialty();
        specialty.setDescription(MATCH_ME);

        Specialty savedSpecialty = new Specialty();
        savedSpecialty.setId(1L);

        //need mock only to return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        //when
        Specialty returnedSpecialty = service.save(specialty);

        //then
        assertThat(returnedSpecialty.getId()).isEqualTo(1L);
    }

    @Test
    void testSaveLambdaNoMatch() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Specialty specialty = new Specialty();
        specialty.setDescription("Not a match");

        Specialty savedSpecialty = new Specialty();
        savedSpecialty.setId(1L);

        //need mock only to return on match MATCH_ME string
        lenient().when(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).thenReturn(savedSpecialty);

        //when
        Specialty returnedSpecialty = service.save(specialty);

        //then
        assertNull(returnedSpecialty);
    }
}