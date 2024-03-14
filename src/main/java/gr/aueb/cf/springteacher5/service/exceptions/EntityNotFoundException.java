package gr.aueb.cf.springteacher5.service.exceptions;

public class EntityNotFoundException extends Exception {

        private static final long serialVersionUIOD = 1L;

        public EntityNotFoundException(Class<?> entityClass, Long id) {
            super("Entity " + entityClass.getSimpleName() + " with id " + id + " was not found.");
        }
}
