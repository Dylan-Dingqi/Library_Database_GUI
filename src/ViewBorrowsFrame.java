import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ViewBorrowsFrame extends JFrame{
	public ViewBorrowsFrame() {
		JFrame f = new JFrame("Lookup Book by Name");
//	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      JLabel label = new JLabel("Username:"); 
	      JTextField t1 = new JTextField("Nayeli51");  
	      JButton b = new JButton("Submit");
	      b.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent arg0) {
				String  username = t1.getText();
				String query = "SELECT library_member.username,borrows.book_id,book_name,checkout_date,return_date "
						+ " FROM library_member, borrows, book "
						+ " WHERE borrows.username=library_member.username "
						+ " AND book.book_id = borrows.book_id "
						+ " AND library_member.username='" + username + "'";
				try (Statement stmt = MainWindow.conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					String result = " ";
					result += String.format("%-20s", "username");
					result += String.format("%-10s", "book_id");
					result += String.format("%-20s", "book_borrowed");
					result += String.format("%-25s", "checkout_date");
					result += String.format("%-20s", "return_by");
					result += "\n ========================================"
							+ "=================================================== \n ";
//					if (!rs.next()) {result = "No Result";}
					while (rs.next()) {
						result += String.format("%-20s",rs.getString("username"))
								+ String.format("%-12s", rs.getString("book_id"))
								+ String.format("%-20s", rs.getString("book_name"))
								+ String.format("%-25s", rs.getString("checkout_date"))
								+ String.format("%-20s", rs.getString("return_date"))
								+ "\n ";
					}
					MainWindow.textField.setText(result);
					f.dispose();
				} catch (SQLException e) {System.out.println(e);}
			 }
	      });
	      label.setBounds(50,50, 200,30);  
	      t1.setBounds(50,80, 200,30);  
	      b.setBounds(50,110,200,30);
	      f.add(label); f.add(t1);  f.add(b);
	      f.setSize(400,200);  
	      f.setLayout(null);  
	      f.setVisible(true);  
	}
}
