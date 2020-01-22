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

public class BorrowFrame extends JFrame{
	public BorrowFrame() {
		JFrame f = new JFrame("Borrow a Book");
//	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      JLabel label1 = new JLabel("Username:");
	      JLabel label2 = new JLabel("Book ID:"); 
	      JLabel label3 = new JLabel("Library Location:"); 
	      JLabel label4 = new JLabel("Checkout Date:");
	      JLabel label5 = new JLabel("Return Date:");
	      JTextField t1 = new JTextField("Nayeli51");
	      JTextField t2 = new JTextField("3");
	      JTextField t3 = new JTextField("5");
	      JTextField t4 = new JTextField("11/12/2019");
	      JTextField t5 = new JTextField("20/01/2021");
	      JButton submit_btn = new JButton("Submit");
	      JButton check_btn = new JButton("Check Availability");
	      submit_btn.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent arg0) {
				 int num_copies = 0;
				try (Statement stmt = MainWindow.conn1.createStatement()) {
					String result = " ";
					String username = t1.getText();
					String book_id = t2.getText();
					String location_id = t3.getText();
					String checkout_date = t4.getText();
					String return_date = t5.getText();
					String query = "SELECT num_copies, address, book_name "
							+ " FROM has, lib_location, book"
							+ " WHERE has.book_id='" + book_id + "' "
							+ " AND book.book_id = has.book_id"
							+ " AND has.location_id='" + location_id + "'";
					ResultSet rs = stmt.executeQuery(query);	
					
					if (!rs.next()) { // Check to see if the book is available at this library
						JOptionPane.showMessageDialog(new JFrame(), "Book not found at library " +location_id);
					}
					else { // Check if there is stock at this library
						num_copies = rs.getInt("num_copies");
						String book_name = rs.getString("book_name");
						String address = rs.getString("address");
						if (num_copies <= 0) {
							JOptionPane.showMessageDialog(new JFrame(), "Book not found at library " +location_id);
						}
						else {
							
							// Check if there is such book in user record
							query = "SELECT * "
									+ " FROM borrows "
									+ " WHERE book_id='" + book_id + "'"
									+ " AND username='" + username + "'";
							rs = stmt.executeQuery(query);
							if (rs.next()) { // The book is already borrowed by the user
								JOptionPane.showMessageDialog(new JFrame(), "User '" + username +"' already borrowed this book");
							}
							else {
								// Insert into user record
								query = "INSERT INTO borrows "
										+ "VALUES ('" + username
										+ "', '" + book_id
										+ "', TO_DATE('" + checkout_date
										+ "', 'DD/MM/YYYY'), TO_DATE('" + return_date
										+ "', 'DD/MM/YYYY'))";
//								System.out.println(query);
								stmt.executeQuery(query);
								
								// Update Library Stock
								num_copies -= 1;
								query = "UPDATE has "
										+ "SET num_copies=" + num_copies
										+ " WHERE has.book_id='" + book_id + "' "
										+ " AND has.location_id='" + location_id + "'";
								JOptionPane.showMessageDialog(new JFrame(), "There are " + num_copies + " copies left"
										+ " of " + book_name
										+ " at " + address);
								stmt.executeQuery(query);

								f.dispose();
							}
						}

						
					}
				} catch (SQLException e) {System.out.println(e);}
			 }
	      });
	      check_btn.addActionListener(new ActionListener() {
				 @Override
				 public void actionPerformed(ActionEvent arg0) {
					 int num_copies = 0;
					try (Statement stmt = MainWindow.conn1.createStatement()) {
//						String result = " ";
//						String username = t1.getText();
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
							num_copies = rs.getInt("num_copies");

							JOptionPane.showMessageDialog(new JFrame(), "There are " + num_copies + " copies"
									+ " of " + rs.getString("book_name") 
									+ " at " + rs.getString("address"));
						}
					} catch (SQLException e) {System.out.println(e);}
				 }
		      });

	      label1.setBounds(50,30, 120,30);  t1.setBounds(170,30,  180,30);
	      label2.setBounds(50,70, 120,30);  t2.setBounds(170,70,  180,30);
	      label3.setBounds(50,110, 120,30); t3.setBounds(170,110, 180,30);
	      label4.setBounds(50,150, 120, 30); t4.setBounds(170,150,180,30);
	      label5.setBounds(50,190, 120, 30); t5.setBounds(170,190,180,30);
	      submit_btn.setBounds(250,240,100,30);
	      check_btn.setBounds(50,240,200,30);
	      f.add(label1); f.add(label2); f.add(label3); f.add(t1); f.add(t2); f.add(t3); f.add(submit_btn); f.add(check_btn);
	      f.add(label4); f.add(label5); f.add(t4); f.add(t5);
	      f.setSize(400,300);  
	      f.setLayout(null);  
	      f.setVisible(true);  
	}
}
