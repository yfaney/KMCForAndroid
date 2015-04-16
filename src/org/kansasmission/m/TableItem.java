package org.kansasmission.m;
/** 
 * This class handles table key and items for access XML data easily.
 * @author Faney
 *
 */
public class TableItem {
	String fieldName;
	String fieldTitle;
	String fieldLink;
	String fieldContents;
	public TableItem(String fieldName, String fieldTitle, String fieldLink, String fieldContents) {
		super();
		this.fieldName = fieldName;
		this.fieldTitle = fieldTitle;
		this.fieldContents = fieldContents;
		this.fieldLink = fieldLink;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldTitle() {
		return fieldTitle;
	}
	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}
	public String getFieldLink() {
		return fieldLink;
	}
	public void setFieldLink(String fieldLink) {
		this.fieldLink = fieldLink;
	}
	public String getFieldContents() {
		return fieldContents;
	}
	public void setFieldContents(String fieldContents) {
		this.fieldContents = fieldContents;
	}
}