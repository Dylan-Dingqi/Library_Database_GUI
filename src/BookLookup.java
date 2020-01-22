import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class BookLookup extends JFrame{
	public BookLookup() {
		JFrame f = new JFrame("Lookup Book by Name");
//	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      JLabel label = new JLabel("Book name:"); 
	      JTextField t1 = new JTextField("The Book");  
	      JButton b = new JButton("Submit");
	      b.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent arg0) {
				String bookname = t1.getText();
				String query = "SELECT book.book_id, book_name, book_year, categories, address, num_copies, lib_location.location_id ";
				query += " FROM book, lib_location, has ";
				query += " WHERE  has.location_id=lib_location.location_id ";
				query += " AND has.book_id=book.book_id ";
				query += " AND  book_name='" + bookname + "'";
				try (Statement stmt = MainWindow.conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					String result = " ";
					result += String.format("%-12s","book_id") + "|";
					result += String.format("%-10s","book_year") + "|";
					result += String.format("%-12s","categories") + "|";
					result += String.format("%-20s","book_name") + "|" ;
					result += String.format("%-12s","num_copies") + "|";
					result += String.format("%-30s","Library Address") + "|";
					result += String.format("%-12s","Library ID") ;
					result += "\n ====================================================="
							+ "======================================================== \n ";
//					if (!rs.next()) {
//						result = "  No Result  ";
//					};
					while (rs.next()) {
						result +=  String.format("%-12s",rs.getString("book_id")) + "|";
						result += String.format("%-10s", rs.getString("book_year")) + "|";
						result += String.format("%-12s", rs.getString("categories")) + "|";
						result += String.format("%-20s", rs.getString("book_name")) + "|" ;
						result += String.format("%-12s", rs.getString("num_copies")) + "|";
						result += String.format("%-30s", rs.getString("address")) + "|" ;
						result += String.format("%-12s", rs.getString("location_id")) ;
						result += "\n ";
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
