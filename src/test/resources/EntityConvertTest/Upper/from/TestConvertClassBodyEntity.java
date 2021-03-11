package jp.co.tis.action;


/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@MappedSuperclass
@Generated(value = {"S2JDBC-Gen 2.4.44", "org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl"}, date = "2011/06/29 10:09:18")
public class TestConvertClassBodyEntity {

    /** シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(length = 128, nullable = false, unique = true)
    public String id;

    /** 名称 */
    @Column(length = 256, nullable = true, unique = false)
    public String name;

    /** 登録日付 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, unique = false)
    public Date insertDate;

    /** 更新日付 */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, unique = false)
    public Date updateDate;

    /** バージョンNO */
    @Version
    @Column(precision = 10, nullable = false, unique = false)
    public Long versionNo;

    @ManyToOne
    @JoinColumn(name = "a", referencedColumnName = "b")
    public OtherEntity otherEntity;

    /** projectList関連プロパティ */
    @OneToMany(mappedBy = "b")
    public List<B> bEntityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

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


}
