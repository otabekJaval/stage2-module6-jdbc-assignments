package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    CustomConnector cm;

    {
        try {
            cm = new CustomConnector();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection connection = cm.getConnection();
    private PreparedStatement ps = null;
    private Statement st = null;

    public static void main(String[] args) {

        SimpleJDBCRepository smr = new SimpleJDBCRepository();
        try {
//            System.out.println("smr.createUser() = " + smr.createUser(new User(null,"Torres","Migel",18))); // passed
//            System.out.println("smr.findUserById(1) = " + smr.findUserById(1L));
//            smr.deleteUser(2L);
//            System.out.println("smr.findAllUser() = " + smr.findAllUser());
//            System.out.println(smr.findUserByName("Jack"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String createUserSQL = "insert into myusers (firstname,lastname,age)values (?,?,?)returning id;";
    private static final String updateUserSQL = "update myusers t set t.firstname=?,t.lastname=?, t.age=?;";
    private static final String deleteUser = "delete from myusers where id=?;";
    private static final String findUserByIdSQL = "select * from myusers t where t.id=?;";
    private static final String findUserByNameSQL = "select * from myusers t where t.firstname=?;";
    private static final String findAllUserSQL = "select * from myusers;";

    public Long createUser(User user) throws SQLException {
        ps = cm.getConnection().prepareStatement(createUserSQL);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();
        return resultSet.getLong("id");
    }

    public User findUserById(Long userId) throws SQLException {
        ps = cm.getConnection().prepareStatement(findUserByIdSQL);
        ps.setLong(1, userId);
        ResultSet resultSet = ps.executeQuery();

        if (!resultSet.next()) {
            return null;
        }

        if (resultSet.isLast()) {
            return null;
        }
        User user = new User();

        user.setAge(resultSet.getInt("age"));
        user.setId(userId);
        user.setFirstName(resultSet.getString("firstname"));
        user.setLastName(resultSet.getString("lastname"));

        return user;
    }

    public User findUserByName(String userName) throws SQLException {
        ps = cm.getConnection().prepareStatement(findUserByNameSQL);
        ps.setString(1, userName);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            User user = new User();

            user.setAge(resultSet.getInt("age"));
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            return user;
        }
        return null;
    }

    public List<User> findAllUser() throws SQLException {
        ps = cm.getConnection().prepareStatement(findAllUserSQL);
        ResultSet resultSet = ps.executeQuery();

        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User();

            user.setAge(resultSet.getInt("age"));
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));

            users.add(user);
        }

        return users;
    }

    public User updateUser() throws SQLException {
        ps = cm.getConnection().prepareStatement(updateUserSQL);
        return findUserById(1L);
    }

    public void deleteUser(Long userId) throws SQLException {
        ps = cm.getConnection().prepareStatement(deleteUser);
        ps.setLong(1, userId);
        ps.execute();
    }
}
