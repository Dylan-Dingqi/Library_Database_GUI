import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ReturnFrame {
	public ReturnFrame() {
		JFrame f = new JFrame("Return a Book");
//	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      JLabel label1 = new JLabel("Username:");
	      JLabel label2 = new JLabel("Book ID:"); 
	      JLabel label3 = new JLabel("Library Location:"); 
	      JTextField t1 = new JTextField("Nayeli51");
	      JTextField t2 = new JTextField("3");
	      JTextField t3 = new JTextField("5");
	      JButton b = new JButton("Submit");
	      b.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent arg0) {
				try (Statement stmt = MainWindow.conn1.createStatement()) {
					String result = " ";
					String username = t1.getText();
					String book_id = t2.getText();
					String location_id = t3.getText();
					String query = "SELECT num_copies, address, book_name "
							+ " FROM has, lib_location, book"
							+ " WHERE has.book_id='" + book_id + "' "
							+ " AND book.book_id = has.book_id"
							+ " AND has.location_id='" + location_id + "'";
					ResultSet rs = stmt.executeQuery(query);	
					
					
					if (!rs.next()) {
						JOptionPane.showMessageDialog(new JFrame(), "Book not found at library " +location_id);
					}
					else {
						String book_name = rs.getString("book_name") ;
						String address = rs.getString("address");
						int num_copies = 1 + rs.getInt("num_copies");
						// Check if there is such book in user record
						query = "SELECT * "
								+ " FROM borrows "
								+ " WHERE book_id='" + book_id + "'"
								+ " AND username='" + username + "'";
						rs = stmt.executeQuery(query);
						if (!rs.next()) {
							JOptionPane.showMessageDialog(new JFrame(), "User '" + username +"' did not borrow this book");
						}
						else {
							// Update library stock
							query = "UPDATE has "
									+ "SET num_copies=" + num_copies
									+ " WHERE has.book_id='" + book_id + "' "
									+ " AND has.location_id='" + location_id + "'";
							JOptionPane.showMessageDialog(new JFrame(), "There are " + num_copies + " copies left"
									+ " of " + book_name
									+ " at " + address);
							stmt.executeQuery(query);
							
							// Remove user's borrow record
							query = "DELETE FROM borrows "
									+ " WHERE username='" + username + "' "
									+ " AND book_id='" + book_id + "' ";
							stmt.executeQuery(query);
							f.dispose();
						}
						
					}
				} catch (SQLException e) {System.out.println(e);}
			 }
	      });
	      label1.setBounds(50,30, 120,30);  t1.setBounds(170,30,  180,30);
	      label2.setBounds(50,70, 120,30);  t2.setBounds(170,70,  180,30);
	      label3.setBounds(50,110, 120,30); t3.setBounds(170,110, 180,30);
	      b.setBounds(250,150,100,30);
	      f.add(label1); f.add(label2); f.add(label3); f.add(t1); f.add(t2); f.add(t3); f.add(b);
	      f.setSize(400,220);  
	      f.setLayout(null);  
	      f.setVisible(true);  
	}
}
