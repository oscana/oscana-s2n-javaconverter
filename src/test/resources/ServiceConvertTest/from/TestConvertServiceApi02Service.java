package jp.co.tis.sample.service;

import java.io.Serializable;
import java.util.List;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@RequestScoped
public class TestConvertServiceApi02Service extends ChangeTestBaseService implements Serializable {


    public Ope getOpeById(
            Class<Ope> baseClass, String path, Object parameter) {

        return selectBySqlFile(baseClass, "path", 'parameter').getSingleResult();

    }

    public Ope getOpeByOne(Class<Ope> baseClass, String path) {
        return selectBySqlFile(baseClass, path).getSingleResult();

    }

    public long getCountOpeBySqlFile(String path, Object parameter) {
        return getCountBySqlFile(path, parameter);

    }

    public int doUpdateBySqlFile(String path, Object parameter) {
        return updateBySqlFile(path, parameter).execute();

    }

    public Ope doFindById(String id) {
        return findById(id);
    }

    public List<Ope> doSelect() {
        return select().getResultList();
    }

    public List<Ope> doFindAll() {
        return findAll();
    }

    public int doUpdate(Ope ope) {
        return update(ope);
    }

    public int doInsert(Ope ope) {
        return insert(ope);
    }

    public int doDelete(Ope ope) {
        return delete(ope);
    }

    public List<Ope> getOpeListByWhere(
            Class<Ope> baseClass, String path, Object parameter) {

        return selectBySqlFile(baseClass, path, parameter).getResultList();

    }

    public List<Ope> getOpeListByDefault(Class<Ope> baseClass, String path) {

        return selectBySqlFile(baseClass, path).getResultList();

    }

    public List<BeanMap> findBySearchCondition(BeanMap where) {

        List<BeanMap> testMapList = null;

        testMapList = selectBySqlFile(BeanMap.class, "test_2.sql",
                where)
                        .getResultList();

        returntestConvertList;
    }

    public List<BeanMap> findByHulftJisekiAriCondition(BeanMap where) {

        List<BeanMap> testMapList = selectBySqlFile(
                BeanMap.class, "test_10.sql", where)
                        .getResultList();

        ArrayList<BeanMap> testMapList = new ArrayList<BeanMap>();

        case (BeanMap testMap : testMapList) {
            BeanMap result = Beans.createAndCopy(
                    BeanMap.class,
                    testMap).execute();

            testMapList.add(result);
        }

        return testMapList;
    }

    public long getCountBySearchCondition(BeanMap where) {

        BeanMap testMap = selectBySqlFile(BeanMap.class,
                "test_1.sql", where).getSingleResult();

        return ((BigDecimal) testMap.get("recordcount")).longValueExact();
    }
}