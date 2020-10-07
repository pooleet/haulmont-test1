package com.haulmont.viev;

import com.haulmont.MyUIO;
import com.haulmont.controller.OrderService;
import com.haulmont.controller.UserService;
import com.haulmont.model.Order;
import com.haulmont.model.User;
import com.haulmont.model.WorkStatus;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import db.SqlOrder;
import db.SqlUser;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MySubOrder extends Window {
    // поле общее окно Сюда все складываем.
    final VerticalLayout layoutWindowVertical = new VerticalLayout();
    // поле с кнопками
    final HorizontalLayout buttons = new HorizontalLayout();
    //окно с VerticalR VerticalL
    final HorizontalLayout HorizontalL = new HorizontalLayout();
    // правое окно
    final VerticalLayout VerticalR = new VerticalLayout();
    // левое окно
    final VerticalLayout VerticalL = new VerticalLayout();
    final RadioButtonGroup<WorkStatus> statusSingle =
            new RadioButtonGroup<>("Статус задания");
    private MyUIO myUIO;
    private Order order;
    private Binder<Order> binderOrder = new Binder<>(Order.class, true);
    // поле ввода задиния
    private TextField description = new TextField("Задание");
    private TextField filter = new TextField("Фильтр");
    private Grid<User> userGrid = new Grid<>(User.class);

    private TextField price = new TextField("Цена работы");
    // Create a text field
    private DateField dateStart = new DateField("Создан/Запланирован");
    private DateField dateStop = new DateField("Выполнен");

    private Label TextCustomer = new Label("Заказчик:");
    private Label TextMechanic = new Label("Механик:");

    private Button save;
    private Button close = new Button("Закрыть");


    /////


    private OrderService service;
    private UserService serviceU;
    private SqlOrder sql;
    private SqlUser sqlU;

    public MySubOrder(MyUIO components, String caption) {

        super(caption);
        setSizeUndefined();
        this.myUIO = components;
        save = new Button(caption);
        bindToBean();
        updateListUser();
        visual();
        buttonRead();
        // автоматически привязывает поля класса к названию field
        // binderOrder.bindInstanceFields(this);


        // Выбраем строку из таблицы

        userGrid.asSingleSelect().addValueChangeListener(event -> {
            String strr = event.getValue().getRole().name();

            String strn = smalName(event);

            if (strr.equals("механик")) {

                order.setIdcM(event.getValue().getId());
                order.setNameM(strn);
                TextMechanic.setValue(strn);
            }
            if (strr.equals("клиент")) {

                order.setIdc(event.getValue().getId());
                order.setNameC(strn);
                TextCustomer.setValue(strn);


            }
        });
    }

    private String smalName(HasValue.ValueChangeEvent<User> event) {

        return event.getValue().getLastName() + " " + event.getValue().getFirstName().charAt(0) + "." + event.getValue().getFatherName().charAt(0) + ".";
    }

    public void setOrder(Order order) {
        this.order = order;
        binderOrder.setBean(order);


        dateStart.setValue(order.getDateStart());
        if (order.getDateStart() == null) {
            dateStart.setValue(LocalDate.now());
            statusSingle.setEnabled(false);
        }
        dateStop.setValue(order.getDateStop());
        price.setValue(order.getPrice().toString());
        description.setValue(order.getDescription());
        TextCustomer.setValue(order.getNameC());
        TextCustomer.setCaption("Клиент :");
        TextMechanic.setValue(order.getNameM());
        TextMechanic.setCaption("Механик :");

    }


    // хребет окна
    private void visual() {


        description.setWidth(600, Unit.PIXELS);
        dateStop.setDateFormat("yyyy-MM-dd");
        dateStart.setDateFormat("yyyy-MM-dd");

        buttons.addComponent(close);
        buttons.addComponent(save);


        statusSingle.setEnabled(true);


        statusSingle.setItems(WorkStatus.values());
        if (dateStop.getValue() == null) {
            statusSingle.setValue(WorkStatus.Запланирован);
        }
        if (dateStop.getValue() != null) {
            if (order.getWorkStatus().equals("Выполнен")) {
                statusSingle.setValue(WorkStatus.Выполнен);
            }
            if (order.getWorkStatus().equals("Принят_клиентом")) {
                statusSingle.setValue(WorkStatus.Принят_клиентом);
            }
        }
        //  single.setItems(WorkStatus.values().toString());

        VerticalL.addComponent(description);
        VerticalL.addComponent(filter);

        filter.setPlaceholder("Поиск...");
        filter.addValueChangeListener(e -> updateListUser());
        filter.setValueChangeMode(ValueChangeMode.LAZY);

        VerticalL.addComponent(userGrid);
        VerticalL.addComponent(statusSingle);
        VerticalL.addComponent(price);

        VerticalR.addComponents(dateStart);
        VerticalR.addComponents(dateStop);
        VerticalR.addComponents(TextCustomer);
        VerticalR.addComponents(TextMechanic);

        HorizontalL.addComponent(VerticalL);
        HorizontalL.addComponent(VerticalR);

        // layoutWindowVertical.addComponent();
        layoutWindowVertical.addComponent(HorizontalL);
        layoutWindowVertical.addComponent(buttons);


        setContent(layoutWindowVertical);

        // закрыть

    }


    private void updateListUser() {

        try {
            serviceU = UserService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<User> user = serviceU.findAll(filter.getValue());

        userGrid.setItems(user);
    }


    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }


    void buttonRead() {
// валидация
        description.addValueChangeListener(e -> valueChange(e));
        price.addValueChangeListener(e -> valueChange(e));
        dateStart.addValueChangeListener(e -> valueChange(e));
        dateStop.addValueChangeListener(e -> valueChange(e));

        // dateStop.addValueChangeListener(e -> valueChange(e));
        // order.addValueChangeListener(e -> valueChange(e));
        // fatherName.addValueChangeListener(e -> valueChange(e));

        //close.addClickListener(event -> close());
        //save.addClickListener(e -> this.save());

    }

    private void valueChange(HasValue.ValueChangeEvent e) {
        binderOrder.validate();
        boolean rez = binderOrder.validate().isOk();
        //System.out.println(rez);
        save.setEnabled(rez);
    }

    // проверка полей
    private void bindToBean() {

        // первый символ обязательно буква):
        Pattern pString = Pattern.compile("^[A-zА-яЁё]{10,200}$");
        binderOrder.forField(description)
                .asRequired("")
                .withValidator(description -> (pString.matcher(description).find()), "2 буквы минимум").bind(Order::getDescription, Order::setDescription);

        Pattern pPrice = Pattern.compile("^[\\d.]+$");
        binderOrder.forField(price).withValidator(price -> (pPrice.matcher(price).find()&& Double.parseDouble(price) > 0), "10.0").bind(Order::getPriceL, Order::setPriceL);

        binderOrder.forField(dateStart).withValidator(dateStart ->((dateStart.isBefore(dateStop.getValue()))||(dateStart.equals(dateStop.getValue()))),"").bind(Order::getDateStart,Order::setDateStart);

    }


}
