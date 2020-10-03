package com.haulmont.viev;


import com.haulmont.MyUIM;
import com.haulmont.controller.MechanicService;
import com.haulmont.model.Mechanic;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.ui.*;
import db.SqlMechanic;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MySubMechanic extends Window {

    final VerticalLayout layoutWindowVertical = new VerticalLayout();
    final HorizontalLayout buttons = new HorizontalLayout();
    private MyUIM myUIM;
    private Mechanic mechanic;
    private Binder<Mechanic> binderMechanic = new Binder<>(Mechanic.class, true);


    private Label id = new Label("id");
    private TextField firstName = new TextField("Имя");
    private TextField lastName = new TextField("Фамилия");
    private TextField fatherName = new TextField("Отчество");
    private Label role = new Label("Роль");
    private TextField price = new TextField("Цена за час");
    private Button save;
    private Button close = new Button("Закрыть");

    private MechanicService service;
    private SqlMechanic sql;

    public MySubMechanic(MyUIM myUIM, String nameButt) {

        super(nameButt);
        setSizeUndefined();
        this.myUIM = myUIM;
        save = new Button(nameButt);
        center();
        setClosable(false);
        setModal(true);
        sql = new SqlMechanic();
        try {

            service = MechanicService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        //
       bindToBean();
        // создаем скелет данных
      visual();
        // следим за кнопками
       buttonRead();


        binderMechanic.bindInstanceFields(this);

    }

    // проверка полей
   private void bindToBean() {
        Pattern pPrice = Pattern.compile("^[\\d.]+$");
        binderMechanic.forField(price)

                .withValidator(price -> (pPrice.matcher(price).find()), "10.0").bind(Mechanic::getPriceL, Mechanic::setPriceL);

        // Имя пользователя (с ограничением 2-20 символов, которыми могут быть буквы и цифры,
        // первый символ обязательно буква):
        Pattern pString = Pattern.compile("^[A-zА-яЁё]{2,20}$");
        binderMechanic.forField(firstName)
                .asRequired("")
                .withValidator(firstName -> (pString.matcher(firstName).find()), "2 буквы минимум").bind(Mechanic::getFirstNameL, Mechanic::setFirstNameL);

        binderMechanic.forField(fatherName)
                .asRequired("")
                .withValidator(fatherName -> (pString.matcher(fatherName).find()), "2 буквы минимум").bind(Mechanic::getFatherNameL, Mechanic::setFatherNameL);

        binderMechanic.forField(lastName)
                .asRequired("")
                .withValidator(lastName -> (pString.matcher(lastName).find()), "2 буквы минимум").bind(Mechanic::getLastNameL, Mechanic::setLastNameL);


    }


    void buttonRead() {
// валидация
        price.addValueChangeListener(e -> valueChange(e));
        firstName.addValueChangeListener(e -> valueChange(e));
        lastName.addValueChangeListener(e -> valueChange(e));
        fatherName.addValueChangeListener(e -> valueChange(e));

        close.addClickListener(event -> close());
        save.addClickListener(e -> this.save());

    }

    private void valueChange(HasValue.ValueChangeEvent<String> e) {
        binderMechanic.validate();
        boolean rez = binderMechanic.validate().isOk();
        //System.out.println(rez);
        save.setEnabled(rez);
    }


    // хребет окна
    private void visual() {

        buttons.addComponent(close);
        buttons.addComponent(save);

        layoutWindowVertical.addComponents(id);

        layoutWindowVertical.addComponent(firstName);
        layoutWindowVertical.addComponent(lastName);
        layoutWindowVertical.addComponent(fatherName);
        layoutWindowVertical.addComponent(role);
        layoutWindowVertical.addComponent(price);
        layoutWindowVertical.addComponent(buttons);


        setContent(layoutWindowVertical);

        // закрыть

    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
        binderMechanic.setBean(mechanic);


        id.setValue(mechanic.getName().getId().toString());
        firstName.setValue(mechanic.getName().getFirstName());
        lastName.setValue(mechanic.getName().getLastName());
        fatherName.setValue(mechanic.getName().getFatherName());
        role.setValue(mechanic.getName().getRole().toString());
    }

    private void save() {
        if (save.getCaption().equals("Изменить") && sql.UpdateMechanic(mechanic)) {
            service.save(mechanic);
            myUIM.updateList();
            close();
        }

        if (save.getCaption().equals("Добавить") && sql.CreateMechanic(mechanic)) {
            try {
                service.loadData();
                myUIM.updateList();
                close();


            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }


    }
}
