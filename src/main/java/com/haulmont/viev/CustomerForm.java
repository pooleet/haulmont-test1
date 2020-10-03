package com.haulmont.viev;

import com.haulmont.controller.CustService;
import com.haulmont.model.CustomerStatus;
import com.haulmont.MyUIC;
import com.haulmont.model.Cust;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout  {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private NativeSelect<CustomerStatus> status = new NativeSelect<>("Status");
    private DateField birthdate = new DateField("Birthday");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");


    private CustService service = CustService.getInstance();
    private Cust cust;
    private MyUIC myUIC;
    private Binder<Cust> binder = new Binder<>(Cust.class);

    public CustomerForm(MyUIC myUIC) {
        this.myUIC = myUIC;

        setSizeUndefined();

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        addComponents(firstName, lastName, email, status, birthdate, buttons);

        status.setItems(CustomerStatus.values());

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());

    }

    public void setCust(Cust cust) {
        this.cust = cust;
        binder.setBean(cust);

        // Show delete button for only customers already in the database
        delete.setVisible(cust.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }



    private void delete() {
        service.delete(cust);
        myUIC.updateList();
        setVisible(false);
    }

    private void save() {
        service.save(cust);
        myUIC.updateList();
        setVisible(false);
    }

}
