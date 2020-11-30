package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    public static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    public static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @Mock
    Model model;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController ownerController;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp(){
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation -> {
                    List<Owner> owners = new ArrayList<>();
                    String name = invocation.getArgument(0);
                    if(name.equals("%Buck%")){
                        owners.add(new Owner(1L, "Joe", "Buck"));
                        return owners;
                    } else if(name.equals("%DontFindMe%")){
                        return owners;
                    } else if(name.equals("%FindMe%")){
                        owners.add(new Owner(1L, "Joe", "Buck"));
                        owners.add(new Owner(2L, "Joe", "Buck2"));
                        return owners;
                    }
                    throw new RuntimeException("Invalid Argument");
                });
    }

    @Test
    void processCreationFormNoErrorsTest(){
        Owner owner = new Owner(5L, "Joe", "Buck");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(ownerService.save(owner)).thenReturn(owner);

        String returnedValue = ownerController.processCreationForm(owner, bindingResult);

        verify(bindingResult).hasErrors();
        verify(ownerService).save(any(Owner.class));
        assertThat(returnedValue).isEqualTo(REDIRECT_OWNERS_5);

    }

    @Test
    void processCreationFormHasErrorsTest(){
        Owner owner = new Owner(5L, "Joe", "Buck");

        when(bindingResult.hasErrors()).thenReturn(true);

        String returnedValue = ownerController.processCreationForm(owner, bindingResult);

        verify(bindingResult).hasErrors();
        verify(ownerService, times(0)).save(any(Owner.class));
        assertThat(returnedValue).isEqualTo(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);

    }

    @Test
    void processFindFormWildcardStringAnnotation() {
        Owner owner = new Owner(5L, "Joe", "Buck");

        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

    @Test
    void processFindFormWildcardNotFound() {
        Owner owner = new Owner(5L, "Joe", "DontFindMe");

        String viewName = ownerController.processFindForm(owner, bindingResult, null);

        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

    @Test
    void processFindFormWildcardFound() {
        Owner owner = new Owner(5L, "Joe", "FindMe");
        InOrder inOrder = inOrder(ownerService, model);

        String viewName = ownerController.processFindForm(owner, bindingResult, model);

        verifyNoMoreInteractions(ownerService);

        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);

        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model, times(1)).addAttribute(anyString(), anyList());
        verifyNoMoreInteractions(model);
    }
}