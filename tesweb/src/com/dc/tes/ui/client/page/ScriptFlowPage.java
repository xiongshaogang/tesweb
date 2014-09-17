package com.dc.tes.ui.client.page;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dc.tes.ui.client.AppContext;
import com.dc.tes.ui.client.IScriptFlowService;
import com.dc.tes.ui.client.IScriptFlowServiceAsync;
import com.dc.tes.ui.client.common.ServiceHelper;
import com.dc.tes.ui.client.control.ConfigToolBar;
import com.dc.tes.ui.client.control.DistTextField;
import com.dc.tes.ui.client.control.FormContentPanel;
import com.dc.tes.ui.client.control.GridContentPanel;
import com.dc.tes.ui.client.model.GWTScriptFlow;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ScriptFlowPage extends BasePage {
	IScriptFlowServiceAsync scriptFlowService = ServiceHelper.GetDynamicService("scriptFlow", IScriptFlowService.class);
	GWTScriptFlow EditScriptFlow = null;

	GridContentPanel<GWTScriptFlow> panel;
	FormContentPanel<GWTScriptFlow> detailPanel;
	ConfigToolBar configBar;

	public ScriptFlowPage() {
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		panel = new GridContentPanel<GWTScriptFlow>();
		RpcProxy<PagingLoadResult<GWTScriptFlow>> proxy = new RpcProxy<PagingLoadResult<GWTScriptFlow>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<PagingLoadResult<GWTScriptFlow>> callback) {
				scriptFlowService.GetList(GetSystemID(), panel
						.GetSearchCondition(), (PagingLoadConfig) loadConfig,
						callback);
			}
		};

		panel.setProxy(proxy);
		panel.setColumns(GetColumnConfig());
		panel.DrowSearchBar();
		panel.DrowGridView(GWTScriptFlow.N_Desc);

		configBar = new ConfigToolBar();
		configBar.initPageToolBar(panel.getLoader());
		configBar.AddWidget(new FillToolItem());
		configBar.AddNewBtn("btnAdd", AddHandler());
		configBar.AddEditBtn("btnEdit", EditHandler());
		configBar.AddDelBtn("btnDel", DelHandler());
		InitBtnConfigBar(configBar);
		panel.setBottomBar(configBar);
		add(panel);

		detailPanel = new FormContentPanel<GWTScriptFlow>();
		detailPanel.setBindInfo(GetDetailHashMap());
		panel.setDetailForm(detailPanel);
		add(detailPanel);
	}

	/**
	 * 获得Grid的列配置列表
	 * 
	 * @return Grid的列配置列表
	 */
	private List<ColumnConfig> GetColumnConfig() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		columns.add(new ColumnConfig(GWTScriptFlow.N_Name, "名称",150));
		columns.add(new ColumnConfig(GWTScriptFlow.N_Desc, "描述", 250));
		
		columns.add(GetRenderColumn("viewPackConfig",false, "可执行脚本",80));
		columns.add(GetRenderColumn("RunColumn", true,"执行",40));
		
		return columns;
	}
	
	/**
	 * 获得 脚本定义、执行列
	 * @param iconType	按钮样式名称
	 * @param fireExec	true:执行列 false:定义列
	 * @param title		列表头名称
	 * @param width		列宽
	 * @return			脚本定义、执行列
	 */
	private ColumnConfig GetRenderColumn(final String iconType,final boolean fireExec,String title,int width)
	{
		GridCellRenderer<GWTScriptFlow> gridRender = new GridCellRenderer<GWTScriptFlow>() {
			@Override
			public Object render(final GWTScriptFlow model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<GWTScriptFlow> store, Grid<GWTScriptFlow> grid) {
				String iconID = "icon_" + iconType + model.getID();
				
				boolean isSet = model.getIsSet();
				String iconName = iconType + (isSet ? "" : "_No");
				IconButton b = new IconButton(iconName);
				if(fireExec && !isSet)
				{
					b.setEnabled(false);
					b.setStyleAttribute("cursor", "default");
				}
				
				HtmlContainer html = new HtmlContainer( "<span style = 'margin:0px;padding:0px;' id = '"
						+ iconID + "' ></span>" );
				html.add(b, "#" + iconID);
				b.addSelectionListener(new SelectionListener<IconButtonEvent>() {
							@Override
							public void componentSelected(IconButtonEvent ce) {
								scriptFlowService.GetScript(model.getID(), new AsyncCallback<String>()
										{
											@Override
											public void onFailure(Throwable caught) {
												MessageBox.alert("错误提示", "获取脚本失败", null);
											}

											@Override
											public void onSuccess(String result) {
												model.setScript(result);
												
												String tabId = "scriptFlow_" + model.getID();
												String tabTitle = "可执行脚本" + "[" + model.getName() + "]";
												BasePage_Register page = AppContext.GetRegisterPage(tabId);
												if(page == null)
													page = new ScriptFlowExec(model,fireExec);
												else if(fireExec)
													page.Exec();
												AppContext.GetEntryPoint().AddTabItem(tabId, tabTitle, page);
											}
										});
							}
						});
				return html;
			}
		};
		ColumnConfig column = new ColumnConfig(GWTScriptFlow.N_ID, title, width);
		column.setAlignment(HorizontalAlignment.CENTER);
		column.setSortable(false);
		column.setResizable(false);
		column.setRenderer(gridRender);
		
		return column;
	}

	/**
	 * 获得详细信息绑定的Hash列表
	 * 
	 * @return Map<String,String> 对应 Map<对应绑定的值名称,字段显示名称>
	 */
	public Map<String, String> GetDetailHashMap() {

		Map<String, String> detailMap = new LinkedHashMap<String, String>();
		detailMap.put(GWTScriptFlow.N_Name, "名称");
		detailMap.put(GWTScriptFlow.N_Desc, "描述");
		return detailMap;
	}

	private void CreateEditForm() {
		final Window window = new Window();

		window.setSize(400, 210);
		window.setPlain(true);
		window.setModal(true);
		window.setResizable(false);
		window.setBlinkModal(false);
		window.setLayout(new FitLayout());

		final FormPanel formPanel = new FormPanel();
		formPanel.setBorders(false);
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		formPanel.setPadding(5);

		FormData formData = new FormData("90%");
		String labelStyle = "width:100px;";
		final DistTextField tfName = new DistTextField(EditScriptFlow, EditScriptFlow.getName(), "脚本名称");
		tfName.setLabelStyle(labelStyle);
		tfName.setMaxLength(32);
		formPanel.add(tfName, formData);

		final TextArea tfDesc = new TextArea();
		tfDesc.setFieldLabel("脚本描述");
		tfDesc.setHeight(100);
		tfDesc.setLabelStyle(labelStyle);
		formPanel.add(tfDesc, formData);
		
		Button btnOK = new Button("确定", new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				if (!formPanel.isValid())
					return;
			
				
				EditScriptFlow.SetBasicInfo(tfName.getValue(), tfDesc.getValue());
				
				scriptFlowService.SaveScriptFlow(EditScriptFlow,
						new AsyncCallback<Boolean>() {
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
								MessageBox.alert("错误信息", "保存失败", null);
							}

							@SuppressWarnings("deprecation")
							public void onSuccess(Boolean suc) {
								
								panel.loaderReLoad(EditScriptFlow.IsNew());
								if (suc)
									window.close();
								else {
									tfName.focus();
									tfName.EnforceValidate();
								}
							}
						});
			}
		});
		window.addButton(btnOK);

		window.addButton(new Button("取消", new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("deprecation")
			public void componentSelected(ButtonEvent ce) {
				window.close();
			}
		}));
		window.add(formPanel);

		if (EditScriptFlow.IsNew()) {
			window.setHeading("新增脚本基本信息");
		} else {
			tfName.setValue(EditScriptFlow.getName());
			tfDesc.setValue(EditScriptFlow.getDesc());
			window.setHeading("编辑脚本基本信息");
		}

		window.show();
	}

	private SelectionListener<ButtonEvent> AddHandler() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				EditScriptFlow = new GWTScriptFlow(GetSystemID());
				CreateEditForm();
			}
		};
	}

	private SelectionListener<ButtonEvent> EditHandler() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {

				List<GWTScriptFlow> selectedItems = panel.getDataGrid()
						.getSelectionModel().getSelectedItems();
				EditScriptFlow = selectedItems.get(0);
				CreateEditForm();
			}
		};
	}

	private Listener<MessageBoxEvent> DelHandler() {
		return new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent be) {
				Button msgBtn = be.getButtonClicked();
				if (msgBtn.getText().equalsIgnoreCase("Yes")) {
					scriptFlowService.DeleteScriptFlow(panel.getSelection(),
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
									MessageBox.alert("错误提示", "删除失败", null);
								}

								public void onSuccess(Void obj) {
									panel.reloadGrid();
								}
							});
				}
			}
		};
	}
}
