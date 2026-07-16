package dataaccess;

public class DataAccess {






    public class MemoryUserDAO implements UserDAO{
        private final Map<String, UserData> users = new HashMap<>();
        public void createUser(UserData u){
            users.put(u.username, u);
        }

        public UserData getUser(String username){
            return users.get(username);
        }

        public void clear(){
            users.clear();
        }
    }
}