package com.haulmont;

import com.haulmont.controller.MechanicService;
import com.haulmont.controller.OrderService;
import com.haulmont.model.Mechanic;
import com.haulmont.model.Order;
import com.haulmont.viev.MySubMechanic;
import com.haulmont.viev.MySubOrder;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import db.SqlMechanic;
import db.SqlOrder;

import javax.servlet.annotation.WebServlet;
@Theme("mytheme")
public class MyUIO extends  UI {
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
    private Grid<Order> mechanicGrid = new Grid<>(Order.class);
    private MySubOrder sub;

    private SqlOrder sql;

    @Override
    protected void init(VaadinRequest vaadinRequest) {


    }





    private void Vizual() {
// таблицы для окон

      //  mechanicGrid.setColumns("name.id", "name.firstName", "name.lastName", "name.role", "countOrder", "price");
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

        layoutWindowVertical.addComponent(layoutButtonUpdate);
        layoutWindowVertical.addComponent(mechanicGrid);

        if (getUI().getUI().toString().contains("MyUIO")){navOrder.setStyleName("Red");}


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
