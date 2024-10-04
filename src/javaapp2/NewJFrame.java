package javaapp2;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author PC
 */
public class NewJFrame extends javax.swing.JFrame {
    /**
     * Creates new form NewJFrame
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus_management?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "pass";
    
    

    public NewJFrame() {
        resultArea = new JTextArea();
        
        initComponents();

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private void displayDriverSalaries(Connection conn, String st_month, String st_year) throws SQLException {

        int month = Integer.parseInt(st_month);
        int year = Integer.parseInt(st_year);

        String query = "SELECT d.name, SUM(CASE WHEN td.role = 'driver' THEN 2 ELSE 1 END * r.complexity_level * 1000) AS salary "
                + "FROM Drivers d "
                + "JOIN Trip_Drivers td ON d.driver_id = td.driver_id "
                + "JOIN Trips t ON td.trip_id = t.trip_id "
                + "JOIN Routes r ON t.route_id = r.route_id "
                + "WHERE MONTH(t.trip_date) = ? "
                + "AND YEAR(t.trip_date) = ? "
                + "GROUP BY d.name";
        
        int success = 0;
        try ( PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    success = 1;
                    System.out.println("Driver: " + rs.getString("name") + ", Salary: " + rs.getDouble("salary"));
                    resultArea.append("\n - Driver: " + rs.getString("name") + ", Salary: " + rs.getDouble("salary"));
                }
                if (success == 0) {
                    resultArea.setText("None found ");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);

        }
    }

    private void displayBusRevenues(Connection conn, String startDate, String endDate) throws SQLException {
        String query = "SELECT b.license_plate, SUM(t.passenger_count * t.ticket_price) AS revenue "
                + "FROM Buses b "
                + "JOIN Trips t ON b.bus_id = t.bus_id "
                + "WHERE t.trip_date BETWEEN ? AND ? "
                + "GROUP BY b.license_plate";
        int success = 0;
        try ( PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    success = 1;
                    System.out.println("Bus: " + rs.getString("license_plate") + ", Revenue: " + rs.getDouble("revenue"));
                    resultArea.append("\n - Bus: " + rs.getString("license_plate") + ", Revenue: " + rs.getDouble("revenue"));
                }
                if (success == 0) {
                    resultArea.setText("None found ");
                }
            }
        }
    }

    private void displayMaintenanceSchedules(Connection conn) throws SQLException {
        
        String query = "SELECT b.license_plate, b.last_maintenance_date, "
                + "DATE_ADD(b.last_maintenance_date, INTERVAL 360 - (SUM(r.distance_km * r.complexity_level) / 100) DAY) AS next_maintenance_date "
                + "FROM Buses b "
                + "JOIN Trips t ON b.bus_id = t.bus_id "
                + "JOIN Routes r ON t.route_id = r.route_id "
                + "GROUP BY b.license_plate, b.last_maintenance_date";
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            List<String> overdueBuses = new ArrayList<>();
            int success = 0;
            while (rs.next()) {
                success = 1;
                String licensePlate = rs.getString("license_plate");
                java.util.Date nextMaintenanceDate = rs.getDate("next_maintenance_date");
                System.out.println("Bus: " + licensePlate + ", Next Maintenance Date: " + nextMaintenanceDate);
                resultArea.append("\n  - Bus: " + licensePlate + ", Next Maintenance Date: " + nextMaintenanceDate);
                if (nextMaintenanceDate.before(new java.util.Date())) {
                    overdueBuses.add(licensePlate);
                }
            }
            if (success == 0) {
                resultArea.setText("None found ");
            }
            if (!overdueBuses.isEmpty()) {
                System.out.println("Overdue Buses: " + overdueBuses);
                resultArea.append("\n  - Overdue Buses: " + overdueBuses);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        salaryBtn = new javax.swing.JButton();
        revenueBtn = new javax.swing.JButton();
        maintainBtn = new javax.swing.JButton();
        monthSal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        yearSal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultArea = new javax.swing.JTextArea();
        startDt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        endDt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Salary month");

        salaryBtn.setText("Display Salary");
        salaryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salaryBtnActionPerformed(evt);
            }
        });

        revenueBtn.setText("Display Bus Revenue");
        revenueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revenueBtnActionPerformed(evt);
            }
        });

        maintainBtn.setText("Display next maintenace date");
        maintainBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maintainBtnActionPerformed(evt);
            }
        });

        monthSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthSalActionPerformed(evt);
            }
        });

        jLabel2.setText("Salary year");

        resultArea.setColumns(20);
        resultArea.setRows(5);
        jScrollPane2.setViewportView(resultArea);

        startDt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDtActionPerformed(evt);
            }
        });

        jLabel3.setText("Start date");

        jLabel4.setText("End date");

        endDt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDtActionPerformed(evt);
            }
        });

        jButton1.setText("Driver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Bus");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Route");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Trips");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(241, 241, 241)
                .addComponent(salaryBtn)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(232, 232, 232)
                .addComponent(revenueBtn)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(32, 32, 32)
                        .addComponent(startDt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(monthSal, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(158, 158, 158)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(endDt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(yearSal, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(maintainBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(monthSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(yearSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(salaryBtn)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(startDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(endDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(revenueBtn)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(13, 13, 13)
                .addComponent(maintainBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salaryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salaryBtnActionPerformed
        // TODO add your handling code here:
        String month = monthSal.getText().trim();
        String year = yearSal.getText().trim();
        
        if (month.isEmpty() || year.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            resultArea.setText("Salary of Drivers for Month: " + month + " , Year: " + year );
            try ( Connection conn = getConnection()) {
                displayDriverSalaries(conn, month, year);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_salaryBtnActionPerformed

    private void revenueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revenueBtnActionPerformed
        // TODO add your handling code here:
        String startDate = startDt.getText().trim();
        String endDate = endDt.getText().trim();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both fields. (yyyy-mm-dd) ", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            resultArea.setText("Revenue of Buses from: " + startDate + " to " + endDate );
            try ( Connection conn = getConnection()) {
                
                displayBusRevenues(conn, startDate, endDate);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Format is (yyyy-mm-dd). \n Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_revenueBtnActionPerformed

    private void maintainBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maintainBtnActionPerformed
        // TODO add your handling code here:

        try ( Connection conn = getConnection()) {
            resultArea.setText("Upcoming Bus Maintenance Schedules" );
            displayMaintenanceSchedules(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_maintainBtnActionPerformed

    private void monthSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthSalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthSalActionPerformed

    private void startDtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startDtActionPerformed

    private void endDtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endDtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DriverFrame frame2 = new DriverFrame();
        frame2.setVisible(true);
        // Optionally, hide JFrame1
//        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BusFrame frame2 = new BusFrame();
        frame2.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        RouteFrame frame2 = new RouteFrame();
        frame2.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        TripFrame frame2 = new TripFrame();
        frame2.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField endDt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton maintainBtn;
    private javax.swing.JTextField monthSal;
    private javax.swing.JTextArea resultArea;
    private javax.swing.JButton revenueBtn;
    private javax.swing.JButton salaryBtn;
    private javax.swing.JTextField startDt;
    private javax.swing.JTextField yearSal;
    // End of variables declaration//GEN-END:variables
}
