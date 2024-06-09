package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import com.mybank.reporting.CustomerReport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class SWINGDemo {
    
    private final JEditorPane log;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;
    
    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(350, 250));
        show = new JButton("Show");
        report = new JButton("Report");
        clients = new JComboBox();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            clients.addItem(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }
    }
    
    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 3));
        
        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(log, BorderLayout.CENTER);
        
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                String accType = current.getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";                
                String custInfo = "<br>&nbsp;<b><span style=\"font-size:2em;\">" + current.getLastName() + ", " +
                        current.getFirstName() + "</span><br><hr>" +
                        "&nbsp;<b>Acc Type: </b>" + accType +
                        "<br>&nbsp;<b>Balance: <span style=\"color:red;\">$" + current.getAccount(0).getBalance() + "</span></b>";
                log.setText(custInfo);                
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerReport customerReport = new CustomerReport();
                StringBuilder reportContent = new StringBuilder();
                reportContent.append("<br><b>CUSTOMERS REPORT</b><br>================<br>");
                for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
                    Customer customer = Bank.getCustomer(i);
                    reportContent.append("<br>Customer: ").append(customer.getLastName()).append(", ").append(customer.getFirstName());
                    for (int j = 0; j < customer.getNumberOfAccounts(); j++) {
                        if (customer.getAccount(j) instanceof SavingsAccount) {
                            reportContent.append("<br>&nbsp;&nbsp;Savings Account: current balance is $").append(customer.getAccount(j).getBalance());
                        } else if (customer.getAccount(j) instanceof CheckingAccount) {
                            reportContent.append("<br>&nbsp;&nbsp;Checking Account: current balance is $").append(customer.getAccount(j).getBalance());
                        } else {
                            reportContent.append("<br>&nbsp;&nbsp;Unknown Account Type: current balance is $").append(customer.getAccount(j).getBalance());
                        }
                    }
                }
                log.setText(reportContent.toString());
            }
        });
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setResizable(false);
        frame.setVisible(true);        
    }
    
    public static void main(String[] args) {
        
        Bank.addCustomer("John", "Doe");
        Bank.addCustomer("Fox", "Mulder");
        Bank.addCustomer("Dana", "Scully");
        Bank.getCustomer(0).addAccount(new CheckingAccount(2000));
        Bank.getCustomer(1).addAccount(new SavingsAccount(1000, 3));
        Bank.getCustomer(2).addAccount(new CheckingAccount(1000, 500));
        
        SWINGDemo demo = new SWINGDemo();        
        demo.launchFrame();
    }
}
