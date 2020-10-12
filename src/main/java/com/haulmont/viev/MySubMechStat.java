package com.haulmont.viev;


import com.haulmont.MyUIM;
import com.haulmont.controller.MechStatService;
import com.haulmont.model.MechStatistic;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import db.SqlMechanic;

import java.sql.SQLException;
import java.util.List;


public class MySubMechStat extends Window {
    final VerticalLayout layoutWindowVertical = new VerticalLayout();
    private Button close = new Button("Закрыть");
    private Grid<MechStatistic> mechGrid = new Grid<>(MechStatistic.class);


    private MechStatService serviceM;
    private SqlMechanic sql;
    private MyUIM myUIM;

    public MySubMechStat(MyUIM components) {
        setSizeUndefined();
        this.myUIM = components;
        updateListMech();
        mechGrid.setWidth(1000,Unit.PIXELS);

       mechGrid.setColumns("id", "name" , "countWork","countNotWork" , "countMoney","countTime");
        layoutWindowVertical.addComponents(mechGrid);




        layoutWindowVertical.addComponents(close);
setContent(layoutWindowVertical);

        close.addClickListener(event -> close());
    }

    private void updateListMech() {
        try {
            serviceM = MechStatService.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<MechStatistic> mechStatistics = serviceM.findAll();

        mechGrid.setItems(mechStatistics);

    }

}
