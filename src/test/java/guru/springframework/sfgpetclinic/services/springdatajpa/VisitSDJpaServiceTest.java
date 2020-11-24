package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        //given
        Set<Visit> visitSet = new HashSet<>();
        Visit visit = new Visit();
        Visit visit2 = new Visit();
        visitSet.add(visit);
        visitSet.add(visit2);
        given(visitRepository.findAll()).willReturn(visitSet);

        //when
        Set<Visit> returnedSet = service.findAll();

        //then
        then(visitRepository).should().findAll();
        assertThat(returnedSet).isNotNull();
        assertThat(returnedSet).hasSize(2);
    }

    @Test
    void findById() {
        //given
        Visit visit = new Visit();
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));

        //when
        Visit returnedVisit = service.findById(1L);

        //then
        then(visitRepository).should().findById(anyLong());
        assertThat(returnedVisit).isNotNull();
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit();
        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        //when
        Visit savedVisit = service.save(new Visit());

        //then
        then(visitRepository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        //given
        Visit visit = new Visit();

        //when
        service.delete(visit);

        //then
        then(visitRepository).should().delete(any(Visit.class));

    }

    @Test
    void deleteById() {
        //given - none

        //when
        service.deleteById(1L);

        //then
        then(visitRepository).should().deleteById(anyLong());
    }

}