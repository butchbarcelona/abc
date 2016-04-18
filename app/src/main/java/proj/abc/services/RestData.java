package proj.abc.services;

import java.util.ArrayList;

/**
 * Created by mbarcelona on 1/22/16.
 */
public class RestData {

  String path;
  ArrayList<NameValuePair> params;
  int progress;

  boolean isImage = false;
  boolean success = false;
  int id;

  public RestData(String path, ArrayList<NameValuePair> params) {
    this.path = path;
    this.params = params;
    this.success = false;
  }

  public boolean isImage() {
    return isImage;
  }

  public void setIsImage(boolean isImage) {
    this.isImage = isImage;
  }

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ArrayList<NameValuePair> getParams() {
    return params;
  }

  public void setParams(ArrayList<NameValuePair> params) {
    this.params = params;
  }
}
