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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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
        then(specialtyRepository).should(times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeastOnce() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
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
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
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
        then(specialtyRepository).should(times(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }
}