package com.haulmont.viev;


import com.haulmont.MyUIC;
import com.haulmont.controller.CustomerService;
import com.haulmont.model.Customer;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.ui.*;
import db.SqlCustomer;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MySubCustomer extends Window {


    // окно
    final VerticalLayout layoutWindowVertical = new VerticalLayout();
    final HorizontalLayout buttons = new HorizontalLayout();
    private MyUIC myUIC;
    private Customer customer;
    private Binder<Customer> binderCustomer = new Binder<>(Customer.class, true);


    private Label id = new Label("id");
    private TextField firstName = new TextField("Имя");
    private TextField lastName = new TextField("Фамилия");
    private TextField fatherName = new TextField("Отчество");
    private Label role = new Label("Роль");
    private TextField phone = new TextField("Номер телефона");
    private Button save;
    private Button close = new Button("Отменить");

    private CustomerService serviceC;
    private SqlCustomer sql;

    private String Caption = "";
    public MySubCustomer(MyUIC myUIC, String nameButt) {
        // тут название окна
        super(nameButt);
        setSizeUndefined();
        this.myUIC = myUIC;
        save = new Button("OK");
        Caption = nameButt;
        center();
        setClosable(false);
        setModal(true);
        sql = new SqlCustomer();
        try {

            serviceC = CustomerService.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //addComponent(phone);
        bindToBean();
        // создаем скелет данных
        visual();
        // следим за кнопками
        buttonRead();


        binderCustomer.bindInstanceFields(this);


    }

    private void valueChange(HasValue.ValueChangeEvent<String> e) {
        binderCustomer.validate();
        boolean rez = binderCustomer.validate().isOk();
        //System.out.println(rez);
        save.setEnabled(rez);
    }

    // проверка полей
    private void bindToBean() {
        Pattern pPhone = Pattern.compile("^7\\d{10}$");
        binderCustomer.forField(phone)
                .asRequired(" 71234567890")
                .withValidator(phone -> (phone.length() == 11) && (pPhone.matcher(phone).find()), "71234567890").bind(Customer::getPhone, Customer::setPhone);

        // Имя пользователя (с ограничением 2-20 символов, которыми могут быть буквы и цифры,
        // первый символ обязательно буква):
        Pattern pString = Pattern.compile("^[A-zА-яЁё]{2,20}$");
        binderCustomer.forField(firstName)
                .asRequired("")
                .withValidator(firstName -> (pString.matcher(firstName).find()), "2 буквы минимум").bind(Customer::getFirstNameL, Customer::setFirstNameL);

        binderCustomer.forField(fatherName)
                .asRequired("")
                .withValidator(fatherName -> (pString.matcher(fatherName).find()), "2 буквы минимум").bind(Customer::getFatherNameL, Customer::setFatherNameL);

        binderCustomer.forField(lastName)
                .asRequired("")
                .withValidator(lastName -> (pString.matcher(lastName).find()), "2 буквы минимум").bind(Customer::getLastNameL, Customer::setLastNameL);


    }


    // связь между формами
    public void setCustomer(Customer customer) {
        this.customer = customer;
        binderCustomer.setBean(customer);
        //binderCustomer.getBinding();

        id.setValue(customer.getName().getId().toString());
        firstName.setValue(customer.getName().getFirstName());
        lastName.setValue(customer.getName().getLastName());
        fatherName.setValue(customer.getName().getFatherName());
        role.setValue(customer.getName().getRole().toString());
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
        layoutWindowVertical.addComponent(phone);
        layoutWindowVertical.addComponent(buttons);


        setContent(layoutWindowVertical);

        // закрыть

    }

    void buttonRead() {
// валидация
        phone.addValueChangeListener(e -> valueChange(e));
        firstName.addValueChangeListener(e -> valueChange(e));
        lastName.addValueChangeListener(e -> valueChange(e));
        fatherName.addValueChangeListener(e -> valueChange(e));

        close.addClickListener(event -> close());
        save.addClickListener(e -> this.save());

    }

    private void save() {
        if (Caption.equals("Изменить") && sql.UpdateCustomer(customer)) {
            serviceC.save(customer);
            myUIC.updateList();
            close();
        }

        if (Caption.equals("Добавить") && sql.CreateCustomer(customer)) {
            try {
                serviceC.loadData();
                myUIC.updateList();
                close();
               // CustomerService.getInstance().loadData();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }



    }
}