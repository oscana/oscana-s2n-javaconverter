package jp.co.tis.sample.service;

import java.io.Serializable;
import java.util.List;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.AssertionUtil;

/**
 * Handlerのテスト用データ
 * 対象Handler：-AssertionHandler
 *            -BeansCopyHander
 *            -BeanUtilHander
 *            -CollectionsUtilHandler
 *            -SingletonS2ContainerHandler
 *
 * @author Rai Shuu
 */
public class TestConvertJavaProgramWithAutoInstall {

    public Object index() {

        /**BeansCopyHanderのテストデータ--start*/
        Beans.copy(tbTest, testForm)
                .excludes("column1", "column2")
                .execute();

        Tbtest tb = Beans.createAndCopy(Tbtest.class, tbtest).execute();

        if(test == 0 ) {
            return Beans.createAndCopy(Tbtest.class, tbtest).execute();
        }

        //変数パターン
        insertForm.deleteEntity = Beans.createAndCopy(
                deleteEntityClass,
                testBean)
                .excludesNull()
                .excludesWhitespace()
                .execute();

        // 「(」があるパターン
        Beans.copy(tbTest, new TestForm())
                .excludes("column1", "column2")
                .execute();

        // 関数があるパターン ※実クラスの取得は難しいで、キャストしない形で変換
        testEntity = Beans.createAndCopy(
                testEntity.getClass(),
                testBean)
                .excludesNull()
                .excludesWhitespace()
                .execute();

        testEntity2 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap2).includes("test").execute();
        
        testEntity3 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap3).prefix("test").execute();
        
        testEntity4 = Beans.createAndCopy(testEntity.getClass(),
                testMap4).excludes("test2").execute();
        
        testEntity5 = Beans.createAndCopy(testEntity.getClass(),
                testMap5).excludesWhitespace().execute();
        
        testEntity6 = Beans.createAndCopy(testEntity.getClass(),
                testMap6).excludesNull().execute();
        
        testEntity7 = Beans.createAndCopy(testEntity.getClass(),
                testMap7).execute();
        
        testEntity8 = Beans.createAndCopy(testEntity.getClass(),
                testMap8).whitespace().execute();
        
        testEntity9 = Beans.createAndCopy(testEntity.getClass(),
                testMap9).excludesNull().execute();
        
        testEntity10 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap10).includes("test").whitespace().execute();
        
        testEntity11 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap11).includes("test").excludesWhitespace().execute();        

        testEntity12 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap12).includes("test").excludesNull().execute();
        
        testEntity13 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap7).excludes("test2").prefix("test").execute();
        
        testEntity14 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap14).includes("test").prefix("test").execute();

        testEntity15 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap15).includes("test").excludes("test2").execute();

        testEntity16 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap16).includes("test").prefix("test").excludes("test2").execute();

        testEntity17 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap17).includes("test").prefix("test").excludesWhitespace().execute();

        testEntity18 = Beans.createAndCopy(
                testEntity.getClass(),
                testMap18).includes("test").prefix("test").excludesNull().execute();

        testEntity19 = Beans.copy(
                testEntity.getClass(),
                testMap19).includes("test").execute();
        
        testEntity20 = Beans.copy(
                testEntity.getClass(),
                testMap20).prefix("test").execute();
        
        testEntity21 = Beans.copy(testEntity.getClass(),
                testMap21).excludes("test2").execute();
        
        testEntity22 = Beans.copy(testEntity.getClass(),
                testMap22).excludesWhitespace().execute();
        
        testEntity23 = Beans.copy(testEntity.getClass(),
                testMap23).excludesNull().execute();
        
        testEntity24 = Beans.copy(testEntity.getClass(),
                testMap24).execute();
        
        testEntity25 = Beans.copy(testEntity.getClass(),
                testMap25).whitespace().execute();
        
        testEntity26 = Beans.copy(testEntity.getClass(),
                testMap26).excludesNull().execute();
        
        testEntity27 = Beans.copy(
                testEntity.getClass(),
                testMap27).includes("test").whitespace().execute();
        
        testEntity28 = Beans.copy(
                testEntity.getClass(),
                testMap28).includes("test").excludesWhitespace().execute();        

        testEntity29 = Beans.copy(
                testEntity.getClass(),
                testMap29).includes("test").excludesNull().execute();
        
        testEntity30 = Beans.copy(
                testEntity.getClass(),
                testMap30).excludes("test2").prefix("test").execute();
        
        testEntity31 = Beans.copy(
                testEntity.getClass(),
                testMap31).includes("test").prefix("test").execute();

        testEntity32 = Beans.copy(
                testEntity.getClass(),
                testMap32).includes("test").excludes("test2").execute();

        testEntity33 = Beans.copy(
                testEntity.getClass(),
                testMap33).includes("test").prefix("test").excludes("test2").execute();

        testEntity34 = Beans.copy(
                testEntity.getClass(),
                testMap34).includes("test").prefix("test").excludesWhitespace().execute();

        testEntity35 = Beans.copy(
                testEntity.getClass(),
                testMap35).includes("test").prefix("test").excludesNull().execute();

        /**BeansCopyHanderのテストデータ--end*/

        /**BeanUtilHanderのテストデータ--start*/
        BeanUtil.copy(testBeanFrom, testBeanTo);
        BeanUtil.copyProperties(testListDto, deptCsv);
        tbjtxbfrportkihonnjho = BeanUtil.createAndCopy(TestBean.class, testMap);
        BeanMap map = BeanUtil.createAndCopy(BeanMap.class, testForm);
        tbjtxbfrportkihonnjho = BeanUtil.createAndCopy(testBean.getClass(), getMap());
        BeanMap map = BeanUtil.createAndCopy(beanMap.getClass(), getForm());
        /**BeanUtilHanderのテストデータ--end*/

        /**SingletonS2ContainerHandlerのテストデータ--start*/
        TestLogic opeRoleLogic = SingletonS2Container.getComponent(TestLogic.class);
        //関数があるパターン ※実クラスの取得は難しいで、キャストしない形で変換
        if(test == 1 ) {
            return SingletonS2Container.getComponent(test.getClass());
        }


        SessionScope scope = SingletonS2Container.getComponent("sessionScope");
        SessionScope scope = SingletonS2Container.getComponent("session" + "Scope");
        SessionScope scope = SingletonS2Container.getComponent(scopeName);
        SessionScope scope = SingletonS2Container.getComponent(scope.getName());

        if(test == 2 ) {
            return SingletonS2Container.getComponent("sessionScope");
        }

        /**SingletonS2ContainerHandlerのテストデータ--end*/

        /**AssertionUtilHandlerのテストデータ--start*/
        AssertionUtil.assertNotNull(msg, obj);
        AssertionUtil.assertNotNull(msg(), getObject());
        AssertionUtil.assertNotNull("error", obj);
        AssertionUtil.assertNotNull("error" + "01", obj);
        AssertionUtil.assertNotNull("error" + msg(), getObject1().getObject2());
        /**AssertionUtilHandlerのテストデータ--end*/

        /**CollectionsUtilHandlerのテストデータ--start*/
        final List<T> list = CollectionsUtil.newArrayList(value);
        final List<T> list = CollectionsUtil.newArrayList(new Collection());

        HashMap<String, String> map = CollectionsUtil.newHashMap();
        /**CollectionsUtilHandlerのテストデータ--end*/

        return "test.jsp";
    }
}