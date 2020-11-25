package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    public static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    public static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController ownerController;

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


}