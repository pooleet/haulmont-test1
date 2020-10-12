package com.haulmont;

import com.haulmont.controller.CustomerService;
import com.haulmont.model.Customer;
import com.haulmont.model.Role;
import com.haulmont.model.User;
import com.haulmont.viev.MySubCustomer;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import db.SqlCustomer;
import db.init.CreateTable;

import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUIC extends UI {


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
    // private MySub form = new MySub(this);
    Customer customer;
    private CustomerService service2;
    // таблицы клиент
    private Grid<Customer> customerGrid = new Grid<>(Customer.class);
    private MySubCustomer subC;

    private SqlCustomer sql;
    private CreateTable crt;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        crt = new CreateTable();
        crt.st = true;
        // визуальные элементы
        Vizual();
        // копки наигации
        navDButton();

        workData();
// sql запросы
        sql = new SqlCustomer();
// скрываем активность ели строка не выделена
        editUpdate.setEnabled(false);
        editDelete.setEnabled(false);

        //Выбираем строку из таблицы
        customerGrid.asSingleSelect().addValueChangeListener(event -> {
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
                setCustomer(event.getValue());
            }
        });

        updateList();

        setContent(layoutWindow);

    }


    public void updateList() {
        try {
            service2 = CustomerService.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Customer> custsomer = service2.findAll();

        customerGrid.setItems(custsomer);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    private void Vizual() {
// таблицы для окон
        customerGrid.setWidth(1000,Unit.PIXELS);
        customerGrid.setColumns("name.id", "name.firstName", "name.lastName", "name.role", "countOrder", "phone");
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
        layoutWindowVertical.addComponent(customerGrid);

        if (getUI().getUI().toString().contains("MyUIC")) {
            navCustomer.setStyleName("Red");
        }

        layoutWindow.addComponent(layoutButtonNavigation);
        layoutWindow.addComponent(layoutWindowVertical);
    }

    // удалить
    private void delete() {
        service2.delete(customer);
        updateList();

    }


    private void navDButton() {
        // Переход на новую вкладку
        BrowserWindowOpener openerM = new BrowserWindowOpener(MyUIM.class);
        openerM.setUrl("../mechanic/");
        openerM.extend(navMechanic);

        // Переход на новую вкладку
        BrowserWindowOpener openerO = new BrowserWindowOpener(MyUIM.class);
        openerO.setUrl("../order/");
        openerO.extend(navOrder);
    }

    // работа с данными
    private void workData() {

        //Изменить
        editUpdate.addClickListener(event -> {
            subC = new MySubCustomer(this, editUpdate.getCaption());
            subC.setCustomer(customer);
            UI.getCurrent().addWindow(subC);
        });

        //Добавить
        editAdd.addClickListener(event -> {
            subC = new MySubCustomer(this, editAdd.getCaption());
            User user = new User();
            // System.out.println(res.getString("SURNAME"));
            user.setId(0L);
            user.setFirstName("");
            user.setLastName("");
            user.setFatherName("");
            user.setRole(Role.valueOf("клиент"));
            subC.setCustomer(new Customer(user, "", 0));
            UI.getCurrent().addWindow(subC);
        });


// удалить
        editDelete.addClickListener(event -> {
            if (sql.deleteCustomer(customer)) {
                delete();
            }
        });

    }

    @WebServlet(urlPatterns = "/*", name = "MyUICServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUIC.class, productionMode = false)
    public static class MyUICServlet extends VaadinServlet {
    }
}
