package in.home.user.ui.module.framework.component.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class ButtonUtil {

    public static Button getDefaultButton(String label,
                                          ComponentEventListener<ClickEvent<Button>> clickListener) {
        return new Button(label, clickListener);
    }

    public static Button getPrimaryButton(String label,
                                          ComponentEventListener<ClickEvent<Button>> clickListener) {
        Button button = new Button(label, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    public static Button getErrorButton(String label,
                                        ComponentEventListener<ClickEvent<Button>> clickListener) {
        Button button = new Button(label, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        return button;
    }

    public static Button getSuccessButton(String label,
                                          ComponentEventListener<ClickEvent<Button>> clickListener) {
        Button button = new Button(label, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        return button;
    }

    public static Button getErrorSecondaryButton(String label,
                                                 ComponentEventListener<ClickEvent<Button>> clickListener) {
        Button button = new Button(label, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return button;
    }
}
