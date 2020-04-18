package sandbox.hibernate;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sandbox.hibernate.model.Message;

public class HelloWorldJPA {

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("HelloWorldPU");

    insert(emf, "Hello world first!");
    insert(emf, "Second");
    insert(emf, "Another message");

    List<Message> messages = selectAllAndUpdateFirst(emf);

    for (Message message : messages) {
      System.out.println(message);
    }

  }

  private static void insert(EntityManagerFactory emf, String text) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    
    Message message = new Message();
    message.setText(text);
    
    entityManager.persist(message);
    
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @SuppressWarnings("unchecked")
  private static List<Message> selectAllAndUpdateFirst(EntityManagerFactory emf) {
    EntityManager entityManager = emf.createEntityManager();
    entityManager.getTransaction().begin();
    
    List<Message> messages = entityManager.createQuery("select m from Message m").getResultList();
    
    messages.get(0).setText("Updated Hello world text!");
    
    entityManager.getTransaction().commit();
    entityManager.close();
    
    return messages;
  }

}
