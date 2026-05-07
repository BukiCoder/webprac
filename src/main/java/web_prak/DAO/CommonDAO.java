package web_prak.DAO;

import web_prak.filters.CommonFilter;
import web_prak.models.CommonEntity;

import java.util.Collection;
import java.util.List;

public interface CommonDAO<T extends CommonEntity<ID>, ID> {
    T getById(ID id);

    Collection<T> getAll();

    void save(T entity);

    List<T> searchByFilter(CommonFilter minFilter, CommonFilter maxFilter);

    void saveCollection(Collection<T> entities);

    void delete(T entity);

    void deleteById(ID id);

    void update(T entity);
}
