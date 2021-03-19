package jp.co.tis.sample.service;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import oscana.s2n.seasar.framework.beans.util.Copy;
import oscana.s2n.seasar.framework.beans.util.CreateAndCopy;
import nablarch.core.beans.BeanUtil;
import oscana.s2n.seasar.framework.container.factory.OscanaSingletonContainerFactory;
import java.util.ArrayList;
import java.util.HashMap;


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
        new Copy(tbTest, testForm).excludes("column1", "column2").execute();

        Tbtest tb = new CreateAndCopy<Tbtest>(Tbtest.class, tbtest).execute();

        if(test == 0 ) {
            return new CreateAndCopy<Tbtest>(Tbtest.class, tbtest).execute();
        }

        //変数パターン
        insertForm.deleteEntity = new CreateAndCopy<>(deleteEntityClass, testBean).excludesNull().excludesWhitespace().execute();

        // 「(」があるパターン
        new Copy(tbTest, new TestForm()).excludes("column1", "column2").execute();

        // 関数があるパターン ※実クラスの取得は難しいで、キャストしない形で変換
        testEntity = new CreateAndCopy<>(testEntity.getClass(), testBean).excludesNull().excludesWhitespace().execute();

        testEntity2 = new CreateAndCopy<>(testEntity.getClass(), testMap2).includes("test").execute();

        testEntity3 = new CreateAndCopy<>(testEntity.getClass(), testMap3).prefix("test").execute();

        testEntity4 = new CreateAndCopy<>(testEntity.getClass(), testMap4).excludes("test2").execute();

        testEntity5 = new CreateAndCopy<>(testEntity.getClass(), testMap5).excludesWhitespace().execute();

        testEntity6 = new CreateAndCopy<>(testEntity.getClass(), testMap6).excludesNull().execute();

        testEntity7 = new CreateAndCopy<>(testEntity.getClass(), testMap7).execute();

        testEntity8 = new CreateAndCopy<>(testEntity.getClass(), testMap8).whitespace().execute();

        testEntity9 = new CreateAndCopy<>(testEntity.getClass(), testMap9).excludesNull().execute();

        testEntity10 = new CreateAndCopy<>(testEntity.getClass(), testMap10).includes("test").whitespace().execute();

        testEntity11 = new CreateAndCopy<>(testEntity.getClass(), testMap11).includes("test").excludesWhitespace().execute();

        testEntity12 = new CreateAndCopy<>(testEntity.getClass(), testMap12).includes("test").excludesNull().execute();

        testEntity13 = new CreateAndCopy<>(testEntity.getClass(), testMap7).excludes("test2").prefix("test").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity14 = new CreateAndCopy<>(testEntity.getClass(), testMap14).includes("test").prefix("test").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity15 = new CreateAndCopy<>(testEntity.getClass(), testMap15).includes("test").excludes("test2").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity16 = new CreateAndCopy<>(testEntity.getClass(), testMap16).includes("test").prefix("test").excludes("test2").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity17 = new CreateAndCopy<>(testEntity.getClass(), testMap17).includes("test").prefix("test").excludesWhitespace().execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity18 = new CreateAndCopy<>(testEntity.getClass(), testMap18).includes("test").prefix("test").excludesNull().execute();

        testEntity19 = new Copy(testEntity.getClass(), testMap19).includes("test").execute();

        testEntity20 = new Copy(testEntity.getClass(), testMap20).prefix("test").execute();

        testEntity21 = new Copy(testEntity.getClass(), testMap21).excludes("test2").execute();

        testEntity22 = new Copy(testEntity.getClass(), testMap22).excludesWhitespace().execute();

        testEntity23 = new Copy(testEntity.getClass(), testMap23).excludesNull().execute();

        testEntity24 = new Copy(testEntity.getClass(), testMap24).execute();

        testEntity25 = new Copy(testEntity.getClass(), testMap25).whitespace().execute();

        testEntity26 = new Copy(testEntity.getClass(), testMap26).excludesNull().execute();

        testEntity27 = new Copy(testEntity.getClass(), testMap27).includes("test").whitespace().execute();

        testEntity28 = new Copy(testEntity.getClass(), testMap28).includes("test").excludesWhitespace().execute();

        testEntity29 = new Copy(testEntity.getClass(), testMap29).includes("test").excludesNull().execute();

        testEntity30 = new Copy(testEntity.getClass(), testMap30).excludes("test2").prefix("test").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity31 = new Copy(testEntity.getClass(), testMap31).includes("test").prefix("test").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity32 = new Copy(testEntity.getClass(), testMap32).includes("test").excludes("test2").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity33 = new Copy(testEntity.getClass(), testMap33).includes("test").prefix("test").excludes("test2").execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity34 = new Copy(testEntity.getClass(), testMap34).includes("test").prefix("test").excludesWhitespace().execute();

        // TODO この組み合わせの挙動は移行元と異なる場合があります。詳細はドキュメントを参照
testEntity35 = new Copy(testEntity.getClass(), testMap35).includes("test").prefix("test").excludesNull().execute();

        /**BeansCopyHanderのテストデータ--end*/

        /**BeanUtilHanderのテストデータ--start*/
        BeanUtil.copy(testBeanFrom, testBeanTo);
        BeanUtil.copy(testListDto, deptCsv);
        tbjtxbfrportkihonnjho = BeanUtil.createAndCopy(TestBean.class, testMap);
        BeanMap map = BeanUtil.createAndCopy(BeanMap.class, testForm);
        tbjtxbfrportkihonnjho = BeanUtil.createAndCopy(testBean.getClass(), getMap());
        BeanMap map = BeanUtil.createAndCopy(beanMap.getClass(), getForm());
        /**BeanUtilHanderのテストデータ--end*/

        /**SingletonS2ContainerHandlerのテストデータ--start*/
        TestLogic opeRoleLogic = (TestLogic)OscanaSingletonContainerFactory.getContainer().getComponent(TestLogic.class);
        //関数があるパターン ※実クラスの取得は難しいで、キャストしない形で変換
        if(test == 1 ) {
            return OscanaSingletonContainerFactory.getContainer().getComponent(test.getClass());
        }


        SessionScope scope = OscanaSingletonContainerFactory.getContainer().getComponent ("sessionScope");
        SessionScope scope = OscanaSingletonContainerFactory.getContainer().getComponent("session" + "Scope");
        SessionScope scope = OscanaSingletonContainerFactory.getContainer().getComponent(scopeName);
        SessionScope scope = OscanaSingletonContainerFactory.getContainer().getComponent(scope.getName());

        if(test == 2 ) {
            return OscanaSingletonContainerFactory.getContainer().getComponent("sessionScope");
        }

        /**SingletonS2ContainerHandlerのテストデータ--end*/

        /**AssertionUtilHandlerのテストデータ--start*/
        SAssertionUtil.assertNotNull (msg, obj);
        if(test == 3){
            if (obj == null) { throw new NullPointerException(msg); }
        }
        if (getObject() == null) { throw new NullPointerException(msg("1) error")); }
        if (obj == null) { throw new NullPointerException("error"); }
        if (obj == null) { throw new NullPointerException("error"+"01"); }
        if (getObject1().getObject2() == null) { throw new NullPointerException("error"+msg()); }
        /**AssertionUtilHandlerのテストデータ--end*/

        /**CollectionsUtilHandlerのテストデータ--start*/
        final List<T> list = new ArrayList<>(value);
        final List<T> list = new ArrayList<>(new Collection());

        HashMap<String, String> map = new HashMap<>();
        /**CollectionsUtilHandlerのテストデータ--end*/

        return "test.jsp";
    }
}
