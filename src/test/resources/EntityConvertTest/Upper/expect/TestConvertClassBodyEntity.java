package jp.co.tis.action;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;

import javax.persistence.*;



/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Generated(value = {"S2JDBC-Gen 2.4.44", "org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl"}, date = "2011/06/29 10:09:18")
@Entity
@Table(name = "TEST_CONVERT_CLASS_BODY_ENTITY.JAVA")
public class TestConvertClassBodyEntity {

    /** シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /** ID */
    public String id;

    /** 名称 */
    public String name;

    /** 登録日付 */
    public Date insertDate;

    /** 更新日付 */
    public Date updateDate;

    /** バージョンNO */
    public Long versionNo;

    public OtherEntity otherEntity;

    /** projectList関連プロパティ */
    public List<B> bEntityList;

    @Column(name = "ID", length = 128, nullable = false, unique = true)
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME", length = 256, nullable = true, unique = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "INSERT_DATE", nullable = false, unique = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    @Column(name = "UPDATE_DATE", nullable = false, unique = false)
    @Temporal(TemporalType.DATE)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "VERSION_NO", precision = 10, nullable = false, unique = false)
    @Version
    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    // TODO ツールで変換できません
    //@JoinColumn(name = "a", referencedColumnName = "b")
    // TODO ツールで変換できません
    //@ManyToOne
    public OtherEntity getOtherEntity() {
        return otherEntity;
    }

    public void setOtherEntity(OtherEntity otherEntity) {
        this.otherEntity = otherEntity;
    }

    public List<B> getbEntityList() {
        return bEntityList;
    }

    public void setbEntityList(List<B> bEntityList) {
        this.bEntityList = bEntityList;
    }



    /**
     * projectList関連プロパティを取得する
     *
     * @return bEntityList projectList関連プロパティ
     */
    // TODO ツールで変換できません
    //@OneToMany(mappedBy = "b")
    public List<B> getBEntityList() {
        return bEntityList;
    }

    /**
     * projectList関連プロパティを設定する
     *
     * @param bEntityList projectList関連プロパティ
     */
    public void setBEntityList(List<B> bEntityList) {
        this.bEntityList = bEntityList;
    }

}
