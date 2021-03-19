package jp.co.tis.sample.action;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
@MappedSuperclass
@Generated(value = {"X-Gen 2.4.44","org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl"}, date = "2011/06/29 10:09:18")
public class TestConvertEntityApi02Entity {

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


}
