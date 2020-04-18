package sandbox.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import sandbox.hibernate.model.Message;

public class HelloWorldHibernate {

  public static void main(String[] args) {
    SessionFactory sessionFactory = getSessionFactory();

    insert(sessionFactory, "Hello word Hibernate first!");
    insert(sessionFactory, "Second message");
    insert(sessionFactory, "Another message");

    List<Message> messages = selectAllAndUpdateFirst(sessionFactory, "This is updated text");

    messages.forEach(System.out::println);
  }

  private static SessionFactory getSessionFactory() {
    StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();

    serviceRegistryBuilder
        .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
        .applySetting("hibernate.connection.url", "jdbc:postgresql://localhost/postgres")
        .applySetting("hibernate.connection.user", "postgres")
        .applySetting("hibernate.connection.password", "postgres")
        .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect")
        .applySetting("hibernate.hbm2ddl.auto", "create-drop")
        .applySetting("hibernate.show_sql", "true")
        .applySetting("hibernate.format_sql", "true")
        .applySetting("hibernate.use_sql_comments", "true")
        .applySetting("hibernate.current_session_context_class", "thread");

    StandardServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

    MetadataSources metadataSources = new MetadataSources(serviceRegistry);
    metadataSources.addAnnotatedClass(Message.class);

    MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();

    Metadata metadata = metadataBuilder.build();

    assert metadata.getEntityBindings().size() > 1;

    return metadata.buildSessionFactory();
  }

  private static void insert(SessionFactory sessionFactory, String text) {
    Session session = sessionFactory.getCurrentSession();
    Transaction transaction = session.beginTransaction();

    Message message = new Message();
    message.setText(text);

    session.persist(message);

    transaction.commit();
  }

  @SuppressWarnings("unchecked")
  private static List<Message> selectAllAndUpdateFirst(SessionFactory sessionFactory, String textToUpdate) {
    Session session = sessionFactory.getCurrentSession();
    Transaction transaction = session.beginTransaction();

    List<Message> messages = session.createCriteria(Message.class).list();
    messages.get(0).setText(textToUpdate);

    transaction.commit();

    return messages;
  }

}
