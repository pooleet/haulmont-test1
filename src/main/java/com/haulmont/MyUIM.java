package com.haulmont;


import com.haulmont.controller.MechanicService;
import com.haulmont.model.Mechanic;
import com.haulmont.model.Role;
import com.haulmont.model.User;
import com.haulmont.viev.MySubMechStat;
import com.haulmont.viev.MySubMechanic;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import db.SqlMechanic;

import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.util.List;

@Theme("mytheme")
public class MyUIM extends UI {
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
    //  private Grid<Mechanic> mechanicGrid = new Grid<>(Mechanic.class);

    // таблицы Закз


    // кнопки редактирования
    final Button editAdd = new Button("Добавить");
    final Button editUpdate = new Button("Изменить");
    final Button editDelete = new Button("Удалить");

    final Button editStatistic = new Button("Показать статистику");

    private Mechanic mechanic;
    private MechanicService service;
    // таблицы механик
    private Grid<Mechanic> mechanicGrid = new Grid<>(Mechanic.class);
    private MySubMechanic sub;
    private MySubMechStat subS;
    private SqlMechanic sql;


    @Override

    protected void init(VaadinRequest vaadinRequest) {


        workData();
        Vizual();
        // копки наигации
        navDButton();
// sql запросы
        sql = new SqlMechanic();
// скрываем активность ели строка не выделена
        editUpdate.setEnabled(false);
        editDelete.setEnabled(false);

        //Выбираем строку из таблицы
        mechanicGrid.asSingleSelect().addValueChangeListener(event -> {
            editDelete.setEnabled(false);
            if (event.getValue() == null) {

                editUpdate.setEnabled(false);
            }
            if (event.getValue() != null) {
                editUpdate.setEnabled(true);
                if (event.getValue().getCountOrder() == 0) {
                    editDelete.setEnabled(true);
                }
                //  layoutButtonUpdate.setEnabled(true);
                setMechanic(event.getValue());
            }
        });

        updateList();

        setContent(layoutWindow);


    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }


    public void updateList() {
        try {
            service = MechanicService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Mechanic> mechanic = service.findAll();

        mechanicGrid.setItems(mechanic);
    }


    private void Vizual() {
// таблицы для окон
        mechanicGrid.setWidth(1000,Unit.PIXELS);
        mechanicGrid.setColumns("name.id", "name.firstName", "name.lastName", "name.role", "countOrder", "price");
        editUpdate.setEnabled(false);
        // layoutButtonUpdate.setEnabled(false);
        layoutButtonNavigation.addComponent(navCustomer);
        layoutButtonNavigation.addComponent(navMechanic);
        layoutButtonNavigation.addComponent(navOrder);

        // кнопки редактирования
        layoutButtonUpdate.addComponent(editAdd);
        layoutButtonUpdate.addComponent(editUpdate);
        layoutButtonUpdate.addComponent(editDelete);
        layoutButtonUpdate.addComponent(editStatistic);
        //

        layoutWindowVertical.addComponent(layoutButtonUpdate);
        layoutWindowVertical.addComponent(mechanicGrid);

        if (getUI().getUI().toString().contains("MyUIM")) {
            navMechanic.setStyleName("Red");
        }


        layoutWindow.addComponent(layoutButtonNavigation);
        layoutWindow.addComponent(layoutWindowVertical);
    }

    // удалить
    //   private void delete() {


    //   }


    private void navDButton() {
        // Переход на новую вкладку


        // Переход на новую вкладку
        BrowserWindowOpener openerO = new BrowserWindowOpener(MyUIO.class);
        openerO.setUrl("/order/");
        openerO.extend(navOrder);

        BrowserWindowOpener openerC = new BrowserWindowOpener(MyUIC.class);
        openerC.setUrl("/");
        openerC.extend(navCustomer);
    }

    // работа с данными
    private void workData() {

        //Изменить
        editUpdate.addClickListener(event -> {
            sub = new MySubMechanic(this, editUpdate.getCaption());
            sub.setMechanic(mechanic);
            UI.getCurrent().addWindow(sub);
        });

        //Добавить
        editAdd.addClickListener(event -> {
            sub = new MySubMechanic(this, editAdd.getCaption());
            User user = new User();
            // System.out.println(res.getString("SURNAME"));
            user.setId(0L);
            user.setFirstName("");
            user.setLastName("");
            user.setFatherName("");
            user.setRole(Role.valueOf("механик"));
            sub.setMechanic(new Mechanic(user, 0.0, 0));
            UI.getCurrent().addWindow(sub);
        });


// удалить
        editDelete.addClickListener(event -> {
            if (sql.deleteMechanic(mechanic)) {
                service.delete(mechanic);
                updateList();
            }
        });

        // показать статистику
        editStatistic.addClickListener(event ->{
            subS = new MySubMechStat(this);

            UI.getCurrent().addWindow(subS);
        });
    }


    @WebServlet(urlPatterns = "/mechanic/*", name = "MyUIMServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUIM.class, productionMode = false)
    public static class MyUIMServlet extends VaadinServlet {

    }
}
