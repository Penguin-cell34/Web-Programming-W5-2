package murach;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import util.DBUtil;

public class UserDB {

    public static void insert(User user) {

        EntityManager em =
                DBUtil.getEmFactory()
                        .createEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        try {

            trans.begin();

            em.persist(user);

            trans.commit();

        } catch (PersistenceException e) {

            System.out.println(e);

            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {

            em.close();
        }
    }

    public static void update(User user) {

        EntityManager em =
                DBUtil.getEmFactory()
                        .createEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        try {

            trans.begin();

            em.merge(user);

            trans.commit();

        } catch (PersistenceException e) {

            System.out.println(e);

            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {

            em.close();
        }
    }

    public static void delete(User user) {

        EntityManager em =
                DBUtil.getEmFactory()
                        .createEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        try {

            trans.begin();

            em.remove(em.merge(user));

            trans.commit();

        } catch (PersistenceException e) {

            System.out.println(e);

            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {

            em.close();
        }
    }

    public static User selectUser(String email) {

        EntityManager em =
                DBUtil.getEmFactory()
                        .createEntityManager();

        String qString =
                "SELECT u FROM User u "
                + "WHERE u.email = :email";

        TypedQuery<User> q =
                em.createQuery(
                        qString,
                        User.class
                );

        q.setParameter("email", email);

        try {

            return q.getSingleResult();

        } catch (NoResultException e) {

            return null;

        } finally {

            em.close();
        }
    }

    public static boolean emailExists(String email) {

        User user =
                selectUser(email);

        return user != null;
    }
}