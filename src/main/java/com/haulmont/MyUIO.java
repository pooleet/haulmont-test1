package com.haulmont;

import com.haulmont.controller.OrderService;
import com.haulmont.model.Order;
import com.haulmont.model.WorkStatus;
import com.haulmont.viev.MySubOrder;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.orderedlayout.VerticalLayoutState;
import com.vaadin.ui.*;
import db.SqlOrder;
import javafx.geometry.VerticalDirection;

import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.util.List;

@Theme("mytheme")
public class MyUIO extends UI {
    // общее окно тут все объединяется
    final HorizontalLayout layoutWindow = new HorizontalLayout();
    // определяем окна
    // кнопки Добавить Удалить Обновить
    final HorizontalLayout layoutButtonUpdate = new HorizontalLayout();
    // фильтр для списка заказав
    final HorizontalLayout layoutFilter = new HorizontalLayout();
    // общее вертикльное окно
    final VerticalLayout layoutWindowVertical = new VerticalLayout();
    // навигация по таблицам
    final VerticalLayout layoutButtonNavigation = new VerticalLayout();
    // кнопки навигации
    final Button navCustomer = new Button("Клиент");
    final Button navMechanic = new Button("Механик");
    final Button navOrder = new Button("Заказ");
    // таблицы Механик


    // таблицы Закз


    // кнопки редактирования
    final Button editAdd = new Button("Добавить");
    final Button editUpdate = new Button("Изменить");
    final Button editDelete = new Button("Удалить");

    private Order order;
    private OrderService service;
    // таблицы механик
    private Grid<Order> orderGrid = new Grid<>(Order.class);
    private MySubOrder sub;

    private SqlOrder sql;

    // фильтр

    private TextField fCustomer = new TextField("Клиент");
    private TextField fStatus = new TextField("Статус");
    private TextField fDescription = new TextField("Заказ");
    final Button editSearch = new Button("Поиск");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Vizual();
        workData();
        updateList();
        navDButton();


        sql = new SqlOrder();
// скрываем активность ели строка не выделена
        editUpdate.setEnabled(false);
        editDelete.setEnabled(false);

        //Выбираем строку из таблицы
        orderGrid.asSingleSelect().addValueChangeListener(event -> {
            editDelete.setEnabled(false);
            if (event.getValue() == null) {

                editUpdate.setEnabled(false);
            }
            if (event.getValue() != null) {
                editUpdate.setEnabled(true);

                editDelete.setEnabled(true);

                //  layoutButtonUpdate.setEnabled(true);
                setOrder(event.getValue());
            }
        });

        setContent(layoutWindow);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // работа с данными
    private void workData() {

        //Изменить
        editUpdate.addClickListener(event -> {
            sub = new MySubOrder(this, editUpdate.getCaption());
            sub.setOrder(order);
            UI.getCurrent().addWindow(sub);
        });

        //Добавить
        editAdd.addClickListener(event -> {
            sub = new MySubOrder(this, editAdd.getCaption());

            sub.setOrder(new Order(null, null, "", null, "", "", null, null, 0.0, WorkStatus.Запланирован));
            UI.getCurrent().addWindow(sub);
        });


// удалить
        editDelete.addClickListener(event -> {
            if (sql.deleteOrder(order)) {
                service.delete(order);
                updateList();
            }
        });

        editSearch.addClickListener(e -> updateList());
    }

    public void updateList() {

        try {
            service = OrderService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Order> order = service.findAll(fCustomer.getValue(), fStatus.getValue(),fDescription.getValue());

        orderGrid.setItems(order);
    }


    private void Vizual() {
// таблицы для окон

        orderGrid.setColumns("id", "description", "dateStart", "dateStop", "price", "workStatus",
                "nameM", "nameC","idc","idcM");
        orderGrid.setWidth(1000,Unit.PIXELS);

        editUpdate.setEnabled(false);
        // layoutButtonUpdate.setEnabled(false);
        layoutButtonNavigation.addComponent(navCustomer);
        layoutButtonNavigation.addComponent(navMechanic);
        layoutButtonNavigation.addComponent(navOrder);

        // кнопки редактирования
        layoutButtonUpdate.addComponent(editAdd);
        layoutButtonUpdate.addComponent(editUpdate);
        layoutButtonUpdate.addComponent(editDelete);
        //


        layoutFilter.addComponent(fCustomer);
        layoutFilter.addComponent(fStatus);
        layoutFilter.addComponent(fDescription);

        layoutFilter.addComponent(editSearch);


        layoutWindowVertical.addComponent(layoutFilter);
        layoutWindowVertical.addComponent(layoutButtonUpdate);
        layoutWindowVertical.addComponent(orderGrid);

        if (getUI().getUI().toString().contains("MyUIO")) {
            navOrder.setStyleName("Red");

        }


        layoutWindow.addComponent(layoutButtonNavigation);

        layoutWindow.addComponent(layoutWindowVertical);
    }

    private void navDButton() {
        // Переход на новую вкладку
        BrowserWindowOpener openerM = new BrowserWindowOpener(MyUIM.class);
        openerM.setUrl("/mechanic/");
        openerM.extend(navMechanic);


        BrowserWindowOpener openerC = new BrowserWindowOpener(MyUIC.class);
        openerC.setUrl("/");
        openerC.extend(navCustomer);
    }

    @WebServlet(urlPatterns = "/order/*", name = "MyUIOServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUIO.class, productionMode = false)
    public static class MyUIMServlet extends VaadinServlet {

    }
}
