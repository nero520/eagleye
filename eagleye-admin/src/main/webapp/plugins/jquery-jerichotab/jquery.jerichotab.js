/// <reference path="jquery.js"/>
/*
    	* jerichotab
	 * version: release-2.0.1 (05/13/2009) 
	 * @ jQuery v1.3.*
	 *
	 * Licensed under the GPL:
	 *   http://gplv3.fsf.org
	 *
	 * Copyright 2008, 2009 Jericho [ thisnamemeansnothing[at]gmail.com ] 
    ========================================
    #example:
        
    ========================================
    ========================================
    #API:
        #$.fn.initJerichoTab(Function):
                *renderTo(String): the tab render to('#sample')
                *uniqueId(String): the tab's id(It must be unique)
                *tabs(Array): the tabs will be initialized, whose items will be formated as follows:
                    {
                    **title(String): the tab title text
                    **iconImg(String): the tab icon that displayed from title text,
                    **closeable(Boolean): the switch that controls whether the tab can be closed (true as default)
                    }
                *activeTabIndex(Int): the tab you'd like to select after loading(0 as default)
                *contentHeight(Int): height of the content div tag
                *contentCss(Object): the same as style sheet
                *loadOnce(Boolean): the switch controls if load tab content at the first time(true as default)
                *tabWidth(Int): width of each tab(150 as default)
        #$.fn.jerichoTab.addTab(Function):
                *tabId(String); the unique tab Id(Unused, private)
                *tabFirer(JQuery Object): the object that makes tab shown in a special way
                *title(String): the tab title text
                *data(Object): the tab data to load,including:
                            **dataType:type of data,
                            **dataLink:data link
                                #example(must use as suited):
                                ##formtag:
                                    *dataType:'formtag', 
                                    //***use the html tags in this page
                                    *dataLink:'#example' 
                                    //***id of the tag you'd like to display in this tab
                                ##iframe:
                                    *dataType:'iframe', 
                                    //***use the iframe to load another page
                                    *dataLink:''
                                    //***such as 'iframetemplates/iframe.htm' 
                                    //***the relative url of the page you'd like to display in this tab,
                                    //***and jerichoTab will use an iframe to load it
                                ###html:
                                    *dataType:'html',
                                    //*** load data from html url
                                    *dataLink:''
                                    // *** the relative url of your html page
                                ##ajax:
                                *dataType:'ajax',
                                //***use ajax to load data with asynchronous operations
                                *dataLink:''
                                //*** yes,u can write down your ajax handler url and then jerichotab'll make a callback,
                                //*** so the responseText will be displayed in the content holder(u can use html tags in your server callback datas)
                *onLoadCompleted(Function): fired after the data has been loaded
                *iconImg(String): the tab icon that displayed below title text(relative to...),
                *closeable(Boolean): set whether the tab can be closed(true as default)
    ========================================
*/

//; (function($) {
$.extend($.fn, {
    initJerichoTab: function(setting) {
        var opts = $.fn.extend({
            //the container of jerichotab(is required,  a jQuery format selector String as '.container' or '#container')
            renderTo: null,
            //the unique id of jerichotab(is required and unique, not null)
            uniqueId: null,
            //format your tab data like this: [{title:'',iconImg:'',closeable:true},{title:'',iconImg:'',closeable:true}]
            //it's an Array...
            tabs: [],
            //when the jerichotab has been loaded, the tab you'ld like to display first(start at 0, and 0 as default)
            activeTabIndex: 0,
            //the style sheet of tab content
            contentCss: {
                'height': '500px'
            },
            //if you set this property as true, the data'll be loaded only at the first time when users click the tab
            //in other times jerichotab only swich it's css(display property) from 'none' to 'block'
            loadOnce: true,
            //the tab width (150 as default)
            tabWidth: 150,
            //set an ajaxload effect, jerichotab has provided two choices: 'usebg' | 'righttag'
            //'usebg': control if set a big loading gif in the contentholder
            //'righttag': this will set a small loading gif in the right top of contentholder
            loader: 'righttag'
        }, setting);
        //initialize the jerichotab
        function createJerichoTab() {
            //make sure that a container and uniqueId were provided
            if (opts.renderTo == null) { alert('you must set the \'renderTo\' property\r\t--JeirchoTab'); return; }
            if (opts.uniqueId == null) { alert('you must set the \'uniqueId\' property\r\t--JeirchoTab'); return; }
            if ($('#' + opts.uniqueId).length > 0) { alert('you must set the \'uniqueId\' property as unique\r\t--JeirchoTab'); return; }
            //the jerichotab html tree:
            /* <div class="jericho_tab">
            <div class="tab_pages" >
            <div class="tabs">
            <ul /> ###tabpages here
            </div>
            </div>
            <div class="tab_content">
            <div id="jerichotab_contentholder" class="content" /> ###tabcontents here
            </div>
            </div>
            */
            var jerichotab = $('<div class="jericho_tab"><div class="tab_pages" ><div class="tabs"><ul /></div></div><div class="tab_content"><div id="jerichotab_contentholder" class="content" /></div></div>')
                                            .appendTo($(opts.renderTo));
            //apply contentcss to the contentholder
            $('.tab_content>.content', jerichotab)
                                    .css(opts.contentCss);

            //fill data
            $.fn.jerichoTab = {
                master: jerichotab,
                tabWidth: opts.tabWidth,
                tabPageWidth: $('.tab_pages', jerichotab).width(),
                loader: opts.loader,
                loadOnce: opts.loadOnce,
                tabpage: $('.tab_pages>.tabs>ul', jerichotab),
                addTab: function(tabsetting) {
                    //this function will be open to all users for them to add tab at any time
                    var ps = $.fn.extend({
                        //if there is a DOM that cause the tab to be added to tabpages,
                        //you should pass it to jerichotab, in which way tab'll only be activated when
                        //user click the DOM next time
                        tabFirer: null,
                        title: 'Jericho Tab',
                        //the dataType and dataLink were suited as:
                        //1.formtag:
                        //   dataType:'formtag', 
                        //                  --use the html tags in this page
                        //   dataLink:'#example' 
                        //                  --id of the tag you'ld like to display in this tab
                        //2.iframe:
                        //   dataType:'iframe', 
                        //                  --use the iframe to load another page
                        //   dataLink:''
                        //                  --such as 'iframetemplates/iframe.htm', set 
                        //                  --the relative url of the page u'ld like to display in this tab,
                        //                  --and jerichoTab will use an iframe to load it
                        //3.html:
                        //   dataType:'html',
                        //                  --load html tags from a url
                        //   dataLink:''
                        //                  --the relative url of your html page
                        //4.ajax:
                        //   dataType:'ajax',
                        //                  --use the ajax to load datas with asynchronous operations
                        //    dataLink:''
                        //                  --yes,u can write down your ajax handler url and jerichotab'll make a callback,
                        //                  --so the responseText will be displayed in the content holder(u can use html tags in your server callback datas)
                        data: { dataType: '', dataLink: '',  loadOnce:true },
                        //set the tab icon of each(relative to...)
                        iconImg: '',
                        //whether this tab can be closed(ture as default)
                        closeable: true,
                        //this function will be fired after all data has been loaded
                        onLoadCompleted: null
                    }, tabsetting);
                    //set as the unique tab id and tabFirer tag
                    tagGuid = (typeof tagGuid == 'undefined' ? 0 : tagGuid + 1);
                    var curIndex = tagGuid;
                    //window.console && console.log('%o', tabsetting);
                    //check whether the tabFirer exists or not, and that it has an attribute['jerichotabindex'], then set the tab that tabFirer was connected activated
                    //otherwise attach the jerichotabindex' attribute
                    if (ps.tabFirer != null) {
                        var jerichotabindex = ps.tabFirer.attr('jerichotabindex');
                        if (typeof jerichotabindex != 'undefined' && $('#jerichotab_' + jerichotabindex).length > 0)
                            return $.fn.setTabActive(jerichotabindex).adaptSlider().loadData();
                        ps.tabFirer.attr('jerichotabindex', curIndex);
                    }
                    //newTab html tree:
                    /*
                    <li>
                    <div class="tab_left" >
                    <div class="tab_icon" />
                    <div class="tab_text" >JerichoTab</div>
                    <div class="tab_close">
                    <a href="javascript:void(0)" title="Close">&nbsp;</a>
                    </div>
                    </div>
                    <div class="tab_right">&nbsp;</div>
                    </li>
                    */
					
                    if(ps.closeable==false){
                    var newTab = $('<li ondblclick="jericho.applyCloseEvent()" class="jericho_tabs tab_selected" style="width:0px"  id="jerichotab_' + curIndex + '" dataType="' + ps.data.dataType + '" dataLink="' + ps.data.dataLink + '" loadOnce="'+ps.data.loadOnce+'">' +
                                                    '<div class="tab_left"  style="width:' + (opts.tabWidth) + 'px"  >' +
                                                        '<div class="tab_icon" style="' + (ps.iconImg == '' ? 'backrground:none' : 'background-image:url(' + ps.iconImg + ')') + '">&nbsp;</div>' +
                                                        '<div class="tab_text" title="双击关闭全部"  style="width:' + (opts.tabWidth - 50) + 'px"  >' +((ps.title.cut(opts.tabWidth / 10-1)).indexOf("...")>=0?(ps.title.cut(10)+"..."):(ps.title.cut(opts.tabWidth / 10 - 1)))+ '</div>  ' +
                                                        '<div class="tab_close">' + (ps.closeable ? '<a href="javascript:void(0)" title="关闭">&nbsp;</a>' : '') + '</div>' +
                                                    '</div>' +
                                                    '<div class="tab_right">&nbsp;</div>' +
                                                '</li>')
                                                    .appendTo($.fn.jerichoTab.tabpage)
                                                        .css('opacity', '0')
                                                            .applyHover()
                                                                .applyCloseEvent()
                                                                        .animate({ 'opacity': '1', width: opts.tabWidth }, function() {
                                                                            $.fn.setTabActive(curIndex);
                                                                        });
                                                                        }else{
                                                                        var newTab = $('<li ondblclick=jericho.closeSingle("jerichotab_' + curIndex + '")  class="jericho_tabs tab_selected" style="width:0px"  id="jerichotab_' + curIndex + '" dataType="' + ps.data.dataType + '" dataLink="' + ps.data.dataLink + '" loadOnce="'+ps.data.loadOnce+'">' +
                                                    '<div class="tab_left"  style="width:' + (opts.tabWidth) + 'px"  >' +
                                                        '<div class="tab_icon" style="' + (ps.iconImg == '' ? 'backrground:none' : 'background-image:url(' + ps.iconImg + ')') + '">&nbsp;</div>' +
                                                        '<div class="tab_text" title="' + ps.title + '"  style="width:' + (opts.tabWidth - 50) + 'px"  >' +((ps.title.cut(opts.tabWidth / 10-1)).indexOf("...")>=0?(ps.title.cut(10)+"..."):(ps.title.cut(opts.tabWidth / 10 - 1)))+ '</div>  ' +
                                                        '<div class="tab_close">' + (ps.closeable ? '<a href="javascript:void(0)" title="关闭">&nbsp;</a>' : '') + '</div>' +
                                                    '</div>' +
                                                    '<div class="tab_right">&nbsp;</div>' +
                                                    '<div class="subUl" style="display:none"><p class="tab_close"><a href="javascript:void(0)">关闭当前页</a></p><p><a href=javascript:jericho.applyCloseEventOther("jerichotab_' + curIndex + '")>关闭其他页</a></p><p><a href="javascript:jericho.applyCloseEvent()">关闭全部</a></p></div>'+
                                                '</li>')
                                                    .appendTo($.fn.jerichoTab.tabpage)
                                                        .css('opacity', '0')
                                                            .applyHover()
                                                                .applyCloseEvent()
                                                                        .animate({ 'opacity': '1', width: opts.tabWidth }, function() {
                                                                            $.fn.setTabActive(curIndex);
                                                                        });
                                                                        
                                                                        }
                    //use an Array named "tabHash" to restore the tab information
                    tabHash = (typeof tabHash == 'undefined' ? [] : tabHash);
                    tabHash.push({
                        index: curIndex,
                        tabFirer: ps.tabFirer,
                        tabId: 'jerichotab_' + curIndex,
                        holderId: 'jerichotabholder_' + curIndex,
                        iframeId: 'jerichotabiframe_' + curIndex,
                        onCompleted: ps.onLoadCompleted
                    });
                    return newTab.applySlider();
                }
            };
            $.each(opts.tabs, function(i, n) {
                $.fn.jerichoTab.addTab(n);
            });
            if (tabHash.length == 0)
                jerichotab.css({ 'display': 'none' });
        }
        createJerichoTab();
        $.fn.setTabActive(opts.activeTabIndex).loadData();
        $.fn.jerichoTab.resize = function() {
            $.fn.jerichoTab.tabPageWidth = $(".tab_pages", $.fn.jerichoTab.master).width() - (($(".jericho_slider").length > 0) ? 38 : 0);
            $(".tabs", $.fn.jerichoTab.master).width($.fn.jerichoTab.tabPageWidth).applySlider().adaptSlider();
            $('#jerichotab_contentholder').height($(opts.renderTo).height());
        };
        $(window).resize(function() {
            $.fn.jerichoTab.resize();
        })
        //window.console && console.log('width :' + $.fn.jerichoTab.tabpage.width());
    },
    //activate the tag(orderkey is the tab order, start at 1)
    setTabActiveByOrder: function(orderKey) {
        var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
        if (lastTab.length > 0) lastTab.swapTabEnable();
        return $('#jericho_tabs').filter(':nth-child(' + orderKey + ')').swapTabEnable();
    },
    //activate the tag(orderKey is the tagGuid of each tab)
    setTabActive: function(orderKey) {
        var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
        if (lastTab.length > 0) lastTab.swapTabEnable();
        return $('#jerichotab_' + orderKey).swapTabEnable();
    },
    addEvent: function(e, h) {
        var target = this.get(0);
        if (target.addEventListener) {
            target.addEventListener(e, h, false);
        } else if (target.attachEvent) {
            target.attachEvent('on' + e, h);
        }
    },
    //create an iframe in the contentholder to load pages
    buildIFrame: function(src) {
        return this.each(function() {
            var onComleted = null, jerichotabiframe = '';
            for (var tab in tabHash) {
                if (tabHash[tab].holderId == $(this).attr('id')) {
                    onComleted = tabHash[tab].onCompleted;
                    jerichotabiframe = tabHash[tab].iframeId;
                    break;
                }
            }
            var iframe = $('<iframe id="' + jerichotabiframe + '" name="' + jerichotabiframe + '" src="' + src + '" frameborder="0" scrolling="auto" />')
                                        .css({ width: '100%', height: $(this).parent().height(), border: 0 })
                                            .appendTo($(this));
            //add a listener to the load event
            $('#' + jerichotabiframe).addEvent('load', function() {
                //if onComlete(Function) is not null, then release it
                !!onComleted ? onComleted(arguments[1]) : true;
                $.fn.removeLoader();
            });
        });
    },
    //load data from dataLink
    //use the following function after each tab was activated
    loadData: function() {
        return this.each(function() {
            //show ajaxloader first
            $('#jerichotab_contentholder').showLoader();
            var onComleted = null, holderId = '', tabId = '';
            //search information in tabHash
            for (var tab in tabHash) {
                if (tabHash[tab].tabId == $(this).attr('id')) {
                    onComleted = tabHash[tab].onCompleted;
                    holderId = '#' + tabHash[tab].holderId;
                    tabId = '#' + tabHash[tab].tabId;
                    break;
                }
            }
            var dataType = $(this).attr('dataType');
            var dataLink = $(this).attr('dataLink');
            var loadOnce = $(this).attr('loadOnce');
            //if dataType was undefined, nothing will be done
            if (typeof dataType == 'undefined' || dataType == '' || dataType == 'undefined') { removeLoading(); return; }
            //hide the rest contentholders
            $('#jerichotab_contentholder').children('div[class=curholder]').attr('class', 'holder').css({ 'display': 'none' });
            var holder = $(holderId);
            if (holder.length == 0) {
                //if contentholder DOM hasn't been created, create it immediately
                holder = $('<div class="curholder" id="' + holderId.replace('#', '') + '" />').appendTo($('#jerichotab_contentholder'));
                //load data into holder
                load(holder);
            }
            else {
                holder.attr('class', 'curholder').css({ 'display': 'block' });
                if (loadOnce=="true")
                    removeLoading();
                else {
                    holder.html('');
                    load(holder);
                }
            }

            function load(c) {
                switch (dataType) {
                    case 'formtag':
                        //clone html DOM elements in the page
                        $(dataLink).css('display', 'none');
                        var clone = $(dataLink)
                                                    .clone(true)
                                                        .appendTo(c)
                                                            .css('display', 'block');
                        removeLoading(holder);
                        break;
                    case 'html':
                        //load HTML from page
                        holder.load(dataLink + '?t=' + Math.floor(Math.random()), function() {
                            removeLoading(holder);
                        });
                        break;
                    case 'iframe':
                        //use iframe to load a website
                        holder.buildIFrame(dataLink, holder);
                        break;
                    case 'ajax':
                        //load a remote page using an HTTP request
                        $.ajax({
                            url: dataLink,
                            data: { t: Math.floor(Math.random()) },
                            error: function(r) {
                                holder.html('error! can\'t load data by ajax');
                                removeLoading(holder);
                            },
                            success: function(r) {
                                holder.html(r);
                                removeLoading(holder);
                            }
                        });
                        break;
                }
            }
            function removeLoading(h) {
                !!onComleted ? onComleted(h) : true;
                $.fn.removeLoader();
            }
        });
    },
    //attach the slider event to every tab,
    //so users can slide the tabs when there are too much tabs
    attachSliderEvent: function() {
        return this.each(function() {
            var me = this;
            $(me).hover(function() {
                $(me).swapClass('jericho_slider' + $(me).attr('pos') + '_enable', 'jericho_slider' + $(me).attr('pos') + '_hover');
            }, function() {
                $(me).swapClass('jericho_slider' + $(me).attr('pos') + '_hover', 'jericho_slider' + $(me).attr('pos') + '_enable');
            }).mouseup(function() {
                //filter the sliders in order to prevent users from sliding
                if ($(me).is('[slide=no]')) return;
                //get the css(left) of tabpage(ul elements)
                var offLeft = parseInt($.fn.jerichoTab.tabpage.css('left'));
                //the max css(left) of tabpage
                var maxLeft = tabHash.length * $.fn.jerichoTab.tabWidth - $.fn.jerichoTab.tabPageWidth + 38;
                switch ($(me).attr('pos')) {
                    case 'left':
                        //slide to the left side
                        if (offLeft + $.fn.jerichoTab.tabWidth < 0)
                            $.fn.jerichoTab.tabpage.animate({ left: offLeft + $.fn.jerichoTab.tabWidth });
                        else
                            $.fn.jerichoTab.tabpage.animate({ left: 0 }, function() {
                                $(me).attr({ 'slide': 'no', 'class': 'jericho_sliders jericho_sliderleft_disable' });
                            });
                        $('.jericho_sliders[pos=right]').attr({ 'slide': 'yes', 'class': 'jericho_sliders jericho_sliderright_enable' });
                        break;
                    case 'right':
                        //slide to the right side
                        if (offLeft - $.fn.jerichoTab.tabWidth > -maxLeft)
                            $.fn.jerichoTab.tabpage.animate({ left: offLeft - $.fn.jerichoTab.tabWidth });
                        else
                            $.fn.jerichoTab.tabpage.animate({ left: -maxLeft }, function() {
                                $(me).attr({ 'slide': 'no', 'class': 'jericho_sliders jericho_sliderright_disable' });
                            });
                        $('.jericho_sliders[pos=left]').attr({ 'slide': 'yes', 'class': 'jericho_sliders jericho_sliderleft_enable' });
                        break;
                }
            });
        });
    },
    //create or activate the slider to tabpage
    applySlider: function() {
        return this.each(function() {
            if (typeof tabHash == 'undefined' || tabHash.length == 0) return;
            //get the offwidth of tabpage
            var offWidth = tabHash.length * $.fn.jerichoTab.tabWidth - $.fn.jerichoTab.tabPageWidth + 38;
            if (tabHash.length > 0 && offWidth > 0) {
                //make sure that the parent Div of tabpage was fixed(position:relative)
                //so jerichotab can control the display position of tabpage by using 'left'
                $.fn.jerichoTab.tabpage.parent().css({ width: $.fn.jerichoTab.tabPageWidth - 38 });
                //auto grow the tabpage(ul) width and reset 'left'
                $.fn.jerichoTab.tabpage.css({ width: offWidth + $.fn.jerichoTab.tabPageWidth - 38 }).animate({ left: -offWidth }, function() {
                    //append 'jerichosliders' to the tabpageholder if 'jerichoslider' has't been added
                    if ($('.jericho_sliders').length <= 0) {
                        $.fn.jerichoTab.tabpage.parent()
                            .before($('<div class="jericho_sliders jericho_sliderleft_enable" slide="yes" pos="left" />'));
                        $.fn.jerichoTab.tabpage.parent()
                            .after($('<div class="jericho_sliders jericho_sliderright_disable" pos="right" slide="yes" />'));
                        $('.jericho_sliders').attachSliderEvent();
                    }
                    //$('.jericho_sliders').adaptSlider();
                });
            }
            else if (tabHash.length > 0 && offWidth <= 0) {
                //remove 'jerichosliders' whether the tabs were not go beyond the capacity of tabpageholder
                $('.jericho_sliders').remove();
                $.fn.jerichoTab.tabpage.parent().css({ width: $.fn.jerichoTab.tabPageWidth });
                $.fn.jerichoTab.tabpage.css({ width: -offWidth + $.fn.jerichoTab.tabPageWidth }).animate({ left: 0 });
            }
        });
    },
    //make sure that the slider will be adjusted quickly to the tabpage after tab 'clicking' or tab 'initializing'
    adaptSlider: function() {
        return this.each(function() {
            if ($('.jericho_sliders').length > 0) {
                var offLeft = parseInt($.fn.jerichoTab.tabpage.css('left'));
                var curtag = '#', index = 0;
                for (var t in tabHash) {
                    if (tabHash[t].tabId == $(this).attr('id')) {
                        curtag += tabHash[t].tabId;
                        index = parseInt(t);
                        break;
                    }
                }
                //set the tabpage width
                var tabWidth = $.fn.jerichoTab.tabPageWidth - 38;
                //calculate the distance from the left side of tabpage
                var space_l = $.fn.jerichoTab.tabWidth * index + offLeft;
                //calculate the distance from the right side of tabpage
                var space_r = $.fn.jerichoTab.tabWidth * (index + 1) + offLeft;
                //window.console && console.log(space_l + '||' + space_r);
                function setSlider(pos, enable) {
                    $('.jericho_sliders[pos=' + pos + ']').attr({ 'slide': (enable ? 'yes' : 'no'), 'class': 'jericho_sliders jericho_slider' + pos + '_' + (enable ? 'enable' : 'disable') });
                }
                //slider to right to check whether it's a tab nearby left slider
                if ((space_l < 0 && space_l > -$.fn.jerichoTab.tabWidth) && (space_r > 0 && space_r < $.fn.jerichoTab.tabWidth)) {
                    //left
                    $.fn.jerichoTab.tabpage.animate({ left: -$.fn.jerichoTab.tabWidth * index }, function() {
                        if (index == 0) setSlider('left', false);
                        else setSlider('left', true);
                        setSlider('right', true);
                    });
                }
                //slider to left to check whether it's a tab nearby right slider
                if ((space_l < tabWidth && space_l > tabWidth - $.fn.jerichoTab.tabWidth) && (space_r > tabWidth && space_r < tabWidth + $.fn.jerichoTab.tabWidth)) {
                    //right
                    $.fn.jerichoTab.tabpage.animate({ left: -$.fn.jerichoTab.tabWidth * (index + 1) + tabWidth }, function() {
                        if (index == tabHash.length - 1) setSlider('right', false);
                        else setSlider('left', true);
                        setSlider('left', true);
                    });
                }
            }
        });
    },
    //apply event to the close anchor
    applyCloseEvent: function() {
        return this.each(function() {
            var me = this;
            $('.tab_close>a', this).mouseup(function(e) {
                //prevents further propagation of the current event. 
                e.stopPropagation();
                if ($(this).length == 0) return;
                //remove tab from tabpageholder
                $(me).animate({ 'opacity': '0', width: '0px' }, function() {
                    //make the previous tab selected whether the removed tab was selected
                    var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
                    try{
                    	if (lastTab.attr('id') == $(this).attr('id')) {
                            $(this).prev().swapTabEnable().loadData();
                        }
                    }catch(e){
                    	//alert("1="+e);
                    	}
                    
                    //clear the information of the removed tab from tabHash
                    for (var t in tabHash) {
                    	 try{
                    		 var a1 = tabHash[t].tabId;
                    		 var a2 = "";
                    		 try{
                    			 a2 = $(me).attr('id');
                    		 }catch(e){
                    			 alert("5="+e);
                    			 alert("a2="+a2);
                    		 }
                    		//  alert("a2="+a2);
                      //  if (tabHash[t].tabId == $(me).attr('id')) {
                    if (a1 == a2) {
                            if (tabHash[t].tabFirer != null)
                                tabHash[t].tabFirer.removeAttr('jerichotabindex');
                            tabHash.splice(t, 1);
                        }}catch(e){
                        	 alert("2="+e);
                        	}
                    }
                    //adapt slider
                    $(me).applySlider().remove();
                    //remove contentholder
                    try{
                    $('#jerichotabholder_' + $(me).attr('id').replace('jerichotab_', '')).remove();
                    }catch(e){
                    	//alert("3="+e);
                    	}
                })
            })
        });
    },
    //apply the "hover" effect and "onSelect" event
    applyHover: function() {
        return this.each(function() {
            $(this).hover(function() {
                if ($(this).hasClass('tab_unselect')) $(this).addClass('tab_unselect_h');
            }, function() {
                if ($(this).hasClass('tab_unselect_h')) $(this).removeClass('tab_unselect_h');
            }).mouseup(function() {
                if ($(this).hasClass('tab_selected')) return;
                /*------------------------------------------------*/
                var id=$(this).attr("id");
                  for (var tab in tabHash) {
		                if (tabHash[tab].tabId == id&&tabHash[tab].tabId!="jerichotab_0") {
		                	tabHash[tab].tabFirer.parents("body").find("li>a").each(function(){
		                	$(this).css({ 'background-color': '#fff'});
		                	})
		                    tabHash[tab].tabFirer.css({ 'background-color': '#DBDBDB'});
		                }
           		 }
                 /**/
            	var get_n=$(this).text();
            	$('.fnIn>li>a').each(function(){
            		if($(this).text()==get_n){
            			$(this).css({ 'background-color': '#DBDBDB'});
            		}else{
            			$(this).css({ 'background-color': '#fff'});
            		}
            	})
            /**/
                
                //select this tab and hide the last selected tab
                var lastTab = $.fn.jerichoTab.tabpage.children('li').filter('.tab_selected');
                lastTab.attr('class', 'jericho_tabs tab_unselect');
                $('#jerichotabholder_' + lastTab.attr('id').replace('jerichotab_', '')).css({ 'display': 'none' });
                $(this).attr('class', 'jericho_tabs tab_selected').loadData().adaptSlider();
            });
        });
    },
    //switch the tab between the selected mode and the unselected mode, or v.v...
    swapTabEnable: function() {
        return this.each(function() {
            if ($(this).hasClass('tab_selected'))
                $(this).swapClass('tab_selected', 'tab_unselect');
            else if ($(this).hasClass('tab_unselect'))
                $(this).swapClass('tab_unselect', 'tab_selected');
        });
    },
    //change class from css1 to css2 of DOM
    swapClass: function(css1, css2) {
        return this.each(function() {
            $(this).removeClass(css1).addClass(css2);
        })
    },
    //if it takes a long time to load the data, show a loader to ui
    showLoader: function() {
        return this.each(function() {
            switch ($.fn.jerichoTab.loader) {
                case 'usebg':
                    //add a circular loading gif picture to the background of contentholder
                    $(this).addClass('loading');
                    break;
                case 'righttag':
                    //add a small loading gif picture and a banner to the right top corner of contentholder
                    if ($('#jerichotab_contentholder>.righttag').length == 0)
                        $('<div class="righttag">Loading...</div>').appendTo($(this));
                    else
                        $('#jerichotab_contentholder>.righttag').css({ display: 'block' });
                    break;
            }
        });
    },
    //remove the loader
    removeLoader: function() {
        switch ($.fn.jerichoTab.loader) {
            case 'usebg':
                $('#jerichotab_contentholder').removeClass('loading');
                break;
            case 'righttag':
                $('#jerichotab_contentholder>.righttag').css({ display: 'none' });
                break;
        }
    }
});
//})(jQuery);

String.prototype.cut = function(len) {
    var position = 0;
    var result = [];
    var tale = '';
    for (var i = 0; i < this.length; i++) {
        if (position >= len) {
            tale = '...';
            break;
        }
        if (this.charCodeAt(i) > 255) {
            position += 2;
            result.push(this.substr(i, 1));
        }
        else {
            position++;
            result.push(this.substr(i, 1));
        }
    }
    return result.join("") + tale;
}



//add mouseDown 
$(".tabs ul li").live("contextmenu",function(e){
		
			//alert($(this).attr("id"));
			$(this).children("div[class=subUl]").show();
			$(this).siblings().children("div[class=subUl]").hide()
			
			//$(this).children("div[class=subUl]").blur(function(){
				//$(this).children("div[class=subUl]").hide();
			//})
			return false;
		
			
	
	})

$(function(){
$(document).click(function(){
	$("div[class=subUl]").hide();
})
})