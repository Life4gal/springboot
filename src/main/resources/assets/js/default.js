const footer = "<nav class='navbar navbar-default fixed-bottom justify-content-center' style='z-index:-1;'><div class='navbar-inner navbar-content-center text-center'><p>Powered by eft (easy file transfer).</p></div></nav>";

let globalConfig = {};

let userConfig = {};

/**
 * 服务器响应提示
 */
function responseTip(data) {
    layer.closeAll();
    const json = JSON.parse(data);
    if (json.status === "success") {
        layer.msg("保存成功");
    } else {
        alerts("保存失败，请稍后重新尝试");
    }
}

function checkPassword(password, passwordConfirm) {
    return password.length >= userConfig.password.minLength && password.length <= userConfig.password.maxLength && password === passwordConfirm;
}

function sendVerifyCode(email, eventSrc) {
    if (isEmail(email)) {
        layer.load(1);
        $.post("/common/" + email + "/code", function (data) {
            layer.closeAll();
            const json = JSON.parse(data);
            if (json.status === "success") {
                layer.msg("发送成功，请前往邮箱查看");
                $(eventSrc).attr("disabled", "disabled");
                $(eventSrc).addClass("disabled");
                setTimeout(function () {
                    $(eventSrc).removeAttr("disabled");
                    $(eventSrc).removeClass("disabled");
                }, 60000);
            } else {
                alerts("获取验证码失败，请联系管理员");
            }
        });
    } else {
        alerts("邮箱格式不合法");
    }
}

$(document).ready(function () {
    layer.load(1);
    $.get("/config/global", function (data) {
        layer.closeAll();
        globalConfig = JSON.parse(data);
        /** @namespace globalConfig.loadParticle */
        if (globalConfig.loadParticle) {
            // 加载 particle粒子效果
            particlesJS.load('particles-js', 'js/particles.json', function () {
                console.log('callback - particles.js config loaded');
            });
        }
        /** @namespace globalConfig.background.useImage */
        if (globalConfig.background.useImage) {
            /** @namespace globalConfig.background.listGenerator */
            if (globalConfig.background.listGenerator.enable) {
                let start = globalConfig.background.listGenerator.start;
                const end = globalConfig.background.listGenerator.end;
                const len = end - start + 1;
                const list = new Array(len);
                for (let i = 0; i < len; i++) {
                    /** @namespace globalConfig.background.listGenerator.suffix */
                    list[i] = globalConfig.background.listGenerator.prefix + (start++) + globalConfig.background.listGenerator.suffix;
                }
                globalConfig.background.imageList = list;
            }
            changeBackgroundImage();
        }
        setCSS();
    });
    // 加载页脚
    $("#footer").html(footer);
    $("body").append("<button onclick='changeBackgroundImage();' class='rounded-circle btn btn-light random-image' " +
        "style='position: fixed;width: 3rem;height: 3rem;bottom: 1rem;right: 1rem;'>" +
        "<span class='glyphicon glyphicon-retweet'></span></button>");
});

function changeBackgroundImage() {
    if (globalConfig.background.useImage) {
        let idx;
        if (globalConfig.background.random) {
            idx = Math.floor(Math.random() * globalConfig.background.imageList.length);
        } else {
            /** @namespace globalConfig.background.imageIndex */
            idx = globalConfig.background.imageIndex;
        }
        /** @namespace globalConfig.background.imageList */
        const url = globalConfig.background.imageList[idx];
        if (typeof url !== "undefined") {
            const body = $("body");
            $(body).css("background", "url('" + url + "') no-repeat center center fixed");
            $(body).css("background-size", "cover");
        }
    }
}

function setCSS() {
    for (let m = 0; m < globalConfig.css.length; m++) {
        const node = globalConfig.css[m];
        const tempElement = node.selector;
        let element = [];
        if (tempElement instanceof Array) {
            element = tempElement;
        } else {
            element = element.concat(tempElement);
        }
        const tempItem = node.style;
        let item = [];
        if (tempItem instanceof Array) {
            item = tempItem;
        } else {
            item = item.concat(tempItem);
        }
        for (let j = 0; j < element.length; j++) {
            for (let k = 0; k < item.length; k++) {
                const css = item[k].split(":");
                $(element[j]).css(css[0].trim(), rtrim(css[1].trim(), ";"));
            }
        }
    }
}