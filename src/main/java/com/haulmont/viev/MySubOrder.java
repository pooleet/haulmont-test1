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

import java.sql.SQLException;
import java.time.LocalDate;
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
    private Button close = new Button("Отменить");


    /////


    private OrderService service;
    private UserService serviceU;
    private SqlOrder sql;
    //  private SqlUser sqlU;
 private String Caption = "";

    private String strr = "";
    private String strn = "";

    public MySubOrder(MyUIO components, String caption) {


        super(caption);
        setSizeUndefined();
        this.myUIO = components;
        save = new Button("ОК");
        Caption=caption;
        bindToBean();

        visual();
        buttonRead();
        try {

            service = OrderService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        sql = new SqlOrder();
        // автоматически привязывает поля класса к названию field
        // binderOrder.bindInstanceFields(this);


        // Выбраем строку из таблицы


    }

    private String smalName(HasValue.ValueChangeEvent<User> event) {

        return event.getValue().getLastName() + " " + event.getValue().getFirstName().charAt(0) + "." + event.getValue().getFatherName().charAt(0) + ".";
    }

    // передаем класс ЗАКАЗ
    public void setOrder(Order order) {
        statusSingle.setItems(WorkStatus.values());


        this.order = order;
        binderOrder.setBean(order);


//список людей
        updateListUser();

        dateStart.setValue(order.getDateStart());
        if (order.getDateStart() == null) {
            dateStart.setValue(LocalDate.now());
            //  statusSingle.setEnabled(false);
        }


        dateStop.setValue(order.getDateStop());
        if (order.getDateStop() == null) {
            visualWorkStatus();
        }
       /* if (order.getDateStop() != null) {
            dateStop.setValue(order.getDateStop());
            // visualWorkStatus();
            statusSingle.setValue(order.getWorkStatus());
            order.setWorkStatus(statusSingle.getValue());
        }*/


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

    // активность полей статус выполнения рботы
    private void visualWorkStatus() {

        //System.out.println("MySubOrder.visualWorkStatus1 " + statusSingle.getValue());
        //System.out.println("MySubOrder.visualWorkStatus2 " + statusSingle.getId());
        //if (dateStop.getValue() == null) {
        if (statusSingle.getValue() == null) {
            statusSingle.setValue(WorkStatus.Запланирован);
        }

        if (order.getDateStop() == null) {
            statusSingle.setValue(WorkStatus.Запланирован);
            statusSingle.setEnabled(false);
            order.setWorkStatus(WorkStatus.Запланирован);
        }

        //  if (dateStop.getValue() != null) {
        if (order.getDateStop() != null) {
            statusSingle.setEnabled(true);
            if (statusSingle.getValue().equals(WorkStatus.Запланирован)) {
                statusSingle.setValue(WorkStatus.Выполнен);
            }
            if (order.getWorkStatus().equals("Выполнен")) {
                statusSingle.setValue(WorkStatus.Выполнен);
            }
            if (order.getWorkStatus().equals("Принят_клиентом")) {
                statusSingle.setValue(WorkStatus.Принят_клиентом);
            }
            order.setWorkStatus(statusSingle.getValue());

        }


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


   /* public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
}*/


    void buttonRead() {
// валидация

        description.addValueChangeListener(e -> valueChange(e));
        price.addValueChangeListener(e -> valueChange(e));
        dateStart.addValueChangeListener(e -> valueChange(e));
        dateStop.addValueChangeListener(e -> {
            valueChange(e);
            //  if (order.getDateStop() == null) {
            visualWorkStatus();
            //   }
            //  if (order.getDateStop() != null) {
            //       statusSingle.setEnabled(true);

            //    }


        });
        statusSingle.addValueChangeListener(e -> {
            visualWorkStatus();
        });
// выбор строки
        userGrid.asSingleSelect().addValueChangeListener(event -> {
            strr = event.getSource().getValue().getRole().name();
            strn = smalName(event);

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
            valueChange(event);
        });


        close.addClickListener(event -> close());
        save.addClickListener(e -> save());

    }

    private void valueChange(HasValue.ValueChangeEvent e) {
        binderOrder.validate();
        boolean rez = binderOrder.validate().isOk();
        //System.out.println(rez);
        if (order.getIdc() != null && order.getIdcM() != null && rez) {
            save.setEnabled(true);
        } else save.setEnabled(false);

    }

    // проверка полей
    private void bindToBean() {

        // первый символ обязательно буква):

        binderOrder.forField(description)
                .asRequired("")
                .withValidator(description -> (description.length() > 4), "").bind(Order::getDescription, Order::setDescription);

        Pattern pPrice = Pattern.compile("^[\\d.]+$");
        binderOrder.forField(price).withValidator(price -> (pPrice.matcher(price).find() && Double.parseDouble(price) > 0), "10.0").bind(Order::getPriceL, Order::setPriceL);

        binderOrder.forField(dateStart).withValidator(dateStart -> ((dateStop != null || (dateStart.isBefore(dateStop.getValue())) || (dateStart.equals(dateStop.getValue()))) && dateStart != null), "").bind(Order::getDateStart, Order::setDateStart);

        binderOrder.forField(dateStop).withValidator(dateStop -> (dateStop == null || (dateStart.getValue().isBefore(dateStop)) || (dateStart.getValue().equals(dateStop))), "").bind(Order::getDateStop, Order::setDateStop);


    }


    private void save() {


        if (Caption.equals("Изменить") && sql.UpdateOrder(order)) {
            service.save(order);
            closeW();
        }

        if (Caption.equals("Добавить") && sql.CreateOrder(order)) {
            try {
                service.loadData();
                closeW();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeW() {
        myUIO.updateList();
        close();

    }
}
