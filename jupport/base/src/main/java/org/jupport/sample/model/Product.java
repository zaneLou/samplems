package org.jupport.sample.model;

import org.jupport.model.BaseEntity;

public class Product extends BaseEntity<Integer>{
    private static final long serialVersionUID = 2941102374660542279L;
    // 编号,药品列表名称,ATY,图片1,图片2,图片3,图片4,图片5,图片6,列表说明,二维码,药品单位,TY,药品名称,药品成份,药品性状,功能主治,药品规格,用法用量,不良反应,药品禁忌,注意事项,相互作用,药品储藏,包装,有效期,批准文号,生产企业,SC
    private Integer uid;
    protected FileInfo mainImage;
	protected int pid;
	protected String listName;
	protected Category category;
	protected String statement; //列表说明
	protected String barcode;//二维码
	protected String medicineUnit;//药品单位
	protected String ty;//TY
	protected String medicineName;//药品名称
	protected String composition;//药品成份
	protected String medicineTraits;//药品性状
	protected String function;//功能主治----->search
	protected String specification;//药品规格
	protected String usage;//用法用量
	protected String reaction;//不良反应
	protected String taboo;//药品禁忌
	protected String attention;//注意事项
	protected String interaction;//相互作用
	protected String store;//药品储藏
	protected String packing;//包装
	protected String validityPeriod;//有效期
	protected String approvalNumber;//批准文号
	protected String enterprise;//生产企业
	protected String sc;//SC
	
	/*
	 * Properties
	 */
	
	public String getSubFolder()
	{
		int floder1 = pid/100;
		int floder2 = pid;//%100;
		return ""+floder1+"/"+floder2+"/";
	}
	
	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public FileInfo getMainImage() {
		return mainImage;
	}

	public void setMainImage(FileInfo mainImage) {
		this.mainImage = mainImage;
	}

	public Category getCategory() {
		return category;
	}

	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getMedicineUnit() {
		return medicineUnit;
	}

	public void setMedicineUnit(String medicineUnit) {
		this.medicineUnit = medicineUnit;
	}

	public String getTy() {
		return ty;
	}

	public void setTy(String ty) {
		this.ty = ty;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

	public String getMedicineTraits() {
		return medicineTraits;
	}

	public void setMedicineTraits(String medicineTraits) {
		this.medicineTraits = medicineTraits;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public String getTaboo() {
		return taboo;
	}

	public void setTaboo(String taboo) {
		this.taboo = taboo;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getInteraction() {
		return interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public String getSc() {
		return sc;
	}

	public void setSc(String sc) {
		this.sc = sc;
	}
	
	
}
