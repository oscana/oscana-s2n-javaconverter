package jp.co.tis.sample.action;
import javax.inject.Inject;
import java.io.Serializable;
import oscana.s2n.struts.GenericsUtil;
import nablarch.core.db.connection.DbConnectionContext;
import oscana.s2n.common.ParamFilter;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.Prototype;
import javax.inject.Singleton;



/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@Entity
public class TestConvertEntityApi01Entity {

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


    /**
     * IDを取得する
     *
     * @return id ID
     */
    @Id
    @Column(name = "ID", length = 128, nullable = false, unique = true)
    public String getId() {
        return id;
    }

    /**
     * IDを設定する
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 名称を取得する
     *
     * @return name 名称
     */
    @Column(name = "NAME", length = 256, nullable = true, unique = false)
    public String getName() {
        return name;
    }

    /**
     * 名称を設定する
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 登録日付を取得する
     *
     * @return insertDate 登録日付
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSERT_DATE", nullable = false, unique = false)
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * 登録日付を設定する
     *
     * @param insertDate 登録日付
     */
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 更新日付を取得する
     *
     * @return updateDate 更新日付
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATE_DATE", nullable = false, unique = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 更新日付を設定する
     *
     * @param updateDate 更新日付
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * バージョンNOを取得する
     *
     * @return versionNo バージョンNO
     */
    @Version
    @Column(name = "VERSION_NO", precision = 10, nullable = false, unique = false)
    public Long getVersionNo() {
        return versionNo;
    }

    /**
     * バージョンNOを設定する
     *
     * @param versionNo バージョンNO
     */
    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * 変数を取得する
     *
     * @return otherEntity 変数
     */
    // TODO ツールで変換できません
    //@ManyToOne
    // TODO ツールで変換できません
    //@JoinColumn(name = "a", referencedColumnName = "b")
    public OtherEntity getOtherEntity() {
        return otherEntity;
    }

    /**
     * 変数を設定する
     *
     * @param otherEntity 変数
     */
    public void setOtherEntity(OtherEntity otherEntity) {
        this.otherEntity = otherEntity;
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
