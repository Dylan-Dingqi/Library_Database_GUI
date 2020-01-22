import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame{ 
	private static final long serialVersionUID = 1L;
	private static final int FRAME_HEIGHT = 600;
	private static final int FRAME_WIDTH = 1200;
	private static final String PASSWORD = JOptionPane.showInputDialog("Enter password");
	private static final String CONNECTION_STRING = "jdbc:oracle:thin:j44zhou/" + PASSWORD + "@oracle.scs.ryerson.ca:1521:orcl";
	static Connection conn1 = null;
	static JTextArea textField =  new JTextArea();
	
//	private JPanel pane = new JPanel();
	
	public MainWindow() {
		this.createNorthPanel();
		this.createCenterPanel();
		this.createSouthPanel();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		textField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textField.setText("Database Connection Unsuccessful");
		connectDB();
		
	}
	private void createNorthPanel() {
		JPanel main = new JPanel();
        JButton connect_btn = new JButton("Connect");
        JButton disconnect_btn = new JButton("Disconnect");
        JButton view_all_books_btn = new JButton("View All Books");
        JButton book_lookup_btn = new JButton("Lookup Book by Name");
        JButton view_users_btn = new JButton("View All Users");
        JButton borrows_lookup_btn = new JButton("User Record Lookup");
        JButton borrow_book_btn = new JButton("Borrow Book");
        JButton return_book_btn = new JButton("Return Book");
        connect_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				connectDB();		
			}
        });
        disconnect_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
	            try {
	                if (conn1 != null && !conn1.isClosed()) {
	                    conn1.close();
	                    textField.setText("Connection Closed");
	                }
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
			}
      	
        });
        view_all_books_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String query = "select book_id, book_name, book_year, categories from book";
				try (Statement stmt = conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					String result = " ";
					result += String.format("%-12s", "book_id")
							+ String.format("%-12s", "year")
							+ String.format("%-12s", "category")
							+ String.format("%-20s", "book_name");
					result += "\n =================================================== \n ";
					while (rs.next()) {
						result += String.format("%-12s",rs.getString("book_id"));
						result += String.format("%-12s", rs.getString("book_year"));
						result += String.format("%-12s", rs.getString("categories"));
						result += String.format("%-20s", rs.getString("book_name")) ;
						result += "\n ";
					}
					textField.setText(result);
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
        });
        book_lookup_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new BookLookup();
			}
        });
        view_users_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String query = "SELECT username,full_name,user_email,user_phone "
						+ " FROM library_member";
				try (Statement stmt = conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					String result = " ";
					result += String.format("%-30s", "username")
							+ String.format("%-30s", "full_name")
							+ String.format("%-35s", "user_email")
							+ String.format("%-20s", "user_phone");
					result += "\n ==================================================="
							+ "===================================================== \n ";
					while (rs.next()) {
						result += String.format("%-30s", rs.getString("username"))
								+ String.format("%-30s", rs.getString("full_name"))
								+ String.format("%-35s", rs.getString("user_email"))
								+ String.format("%-20s", rs.getString("user_phone"));
						result += "\n ";
					}
					textField.setText(result);
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
        });
        borrows_lookup_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ViewBorrowsFrame();
			}
        });
        borrow_book_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new BorrowFrame();
			}
        });
        return_book_btn.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent arg0) {
        			new ReturnFrame();
        		}
        });
//        main.add(connect_btn);
        main.add(disconnect_btn);
        main.add(view_all_books_btn);
        main.add(view_users_btn);
        main.add(book_lookup_btn);
        main.add(borrows_lookup_btn);
        main.add(borrow_book_btn);
        main.add(return_book_btn);
        this.add(main, BorderLayout.NORTH);
	}
	private void createCenterPanel() {
        textField.setPreferredSize(new Dimension(200, 100));
        this.add(textField, BorderLayout.CENTER);
	}
	
	private void createSouthPanel() {
		JPanel main = new JPanel();
		JButton b1 = new JButton("CREATE");
		JButton b2 = new JButton("INSERT");
		JButton b3 = new JButton("SELECT");
		JButton b4 = new JButton("DROP");
		JButton b5 = new JButton("Average Book Count");
		
		// Create author table
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = "CREATE TABLE author2 (" + 
						"name VARCHAR2(50) UNIQUE NOT NULL," + 
						"email VARCHAR2(50) UNIQUE NOT NULL," + 
						"phone_number VARCHAR2(50) UNIQUE NOT NULL" + 
						")";
				try (Statement stmt = conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					textField.setText(query + " successfully executed");
				} catch (SQLException e) {System.out.println(e);}
			}
		});
		
		// Insert into author table
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String queries[] = {"INSERT INTO author2 VALUES   ('Wendy S','wendy.s@hotmail.com', '647-888-8888')",
						"INSERT INTO author2 VALUES   ('Lucy Y','lucy.y@hotmail.com', '647-999-9999')",
						"INSERT INTO author2 VALUES   ('Sam X','sam.x@hotmail.com', '647-222-2222')"};
				
				try (Statement stmt = conn1.createStatement()) {
					for (int x = 0; x < queries.length; x++) {
						String query = queries[x];
						ResultSet rs = stmt.executeQuery(query);	
					}
					textField.setText(" Insert queries successfully executed");
				} catch (SQLException e) {System.out.println(e);}	
			}
		});
		
		// Select * From Author Table
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = "SELECT * FROM author2";
				try (Statement stmt = conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					String result = "";
					result += String.format("%-30s", "name")
							+ String.format("%-30s", "email")
							+ String.format("%-20s", "phone_number") 
							+ "\n========================================="
							+ "=========================================\n";
					
					while (rs.next()) {
						result += String.format("%-30s", rs.getString("name"))
								+ String.format("%-30s", rs.getString("email"))
								+ String.format("%-20s", rs.getString("phone_number"))
								+ "\n";
					}
					textField.setText(result);
//					textField.setText(query + " successfully executed");
				} catch (SQLException e) {System.out.println(e);}
				
			}
		});
		
		//Drop Author Table
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = "DROP TABLE author2";
				try (Statement stmt = conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					textField.setText(query + " successfully executed");
				} catch (SQLException e) {System.out.println(e);}
				
			}
		});
		b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = "SELECT MAX(num_copies), MIN(num_copies), AVG(num_copies) FROM has";
				try (Statement stmt = conn1.createStatement()) {
					ResultSet rs = stmt.executeQuery(query);	
					String result= "";
					result += String.format("%-20s", " MAX(num_copies)")
							+ String.format("%-20s", "MIN(num_copies)")
							+ String.format("%-20s", "AVG(num_copies)")
							+ "\n========================================="
							+ "=========================================\n";
					while (rs.next()) {
						result += String.format("%-20s", rs.getString("MAX(num_copies)"))
							+ String.format("%-20s", rs.getString("MIN(num_copies)"))
							+ String.format("%-20s", rs.getString("AVG(num_copies)"));
					}
					textField.setText(result);
				} catch (SQLException e) {System.out.println(e);}
			}
		});
		
		
		main.add(b1); main.add(b2); main.add(b3); main.add(b4); main.add(b5);
		this.add(main, BorderLayout.SOUTH);
	}
	private void connectDB() {
		System.out.println("Connecting");		
        try {
            Class.forName("oracle.jdbc.OracleDriver");		          		         	
             String dbURL1 = CONNECTION_STRING;			
			conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
            		textField.setText("Sucessfully Connected to Database");
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }					
	}

}
