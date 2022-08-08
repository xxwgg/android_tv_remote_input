<?php


//若还没进行access_token.json的创建 须使用版本1进行第一次获取access_token然后切换为版本2
//配置文件中定义了appid以及appsecret
header('Content-type:application/json');

// appid和appsecret定义  替换你自己的
$appid = 'wx appid';
$secret = 'wx secret';


// 先读取你之前存的accessToken文件，如果是第一次进行获取accesstoken可能会报file_get_contents读取这个文件错误
// 因为这个文件首先是并没有创建，你可以先创建一个access_token.json文件放在你的文件夹下，按照json格式填写，例如这个可以直接用
// {"access_token": "17_mUB3j8epUNH2h_gPCO1qmpOpWgssYQgfWWP-LjY1N7nvuLHJnj7WO52x0mY_K5pknlrLRiK5docTfzEIJkIUiVKuZDRNpO2Dl8TpiDhIbMAjEmQR8OB3v4tS0ey4Uj14VsBPFr5lEIfVnkH3HWKeAHARGU", "expires_time": 1548229911}
// 由于我已经有了这个文件，所以我在这就不对这个创建文件操作就行描述了，太过简单了
// 判断没有这个文件，会获取微信公众平台的accesstoken进行存储，所以不用担心

// 读取保存的accessToken json文件
//file_get_contents() 函数是用来将文件的内容读入到一个字符串中的首选方法 类似的还有file()函数
//http://php.net/manual/zh/function.file-get-contents.php
$res = file_get_contents('access_token.json');
// 解码json文件，转为PHP变量
//http://www.runoob.com/php/php-json.html
$result = json_decode($res, true);
// 获取access_token.json中的accesstoken的过期时间以及accesstoken
$expires_time = $result["expires_time"];
$access_token = $result["access_token"];

// 判断现在的时间在上次获得accesstoken时间之后是否在一个半小时之内，不是的话然后需要重新获取accesstoken
if (empty($access_token) || time() > ($expires_time + 5400)) {
    // accesstoken获取api 参数为appid和appsecre
    $url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" . $appid . "&secret=" . $secret;

    // curl会话
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    // 得到一个accesstoken json包
    $output = curl_exec($ch);
    curl_close($ch);

    // 解码
    $result = json_decode($output, true);
    // 获取当前存入accesstoken的时间以及accesstoken
    $access_token = $result["access_token"];
    //把当前获取的时间存入到json文件里面
    $expires_time = time();
    // 存入到json文件 方式为覆盖
    file_put_contents('access_token.json', '{"access_token": "' . $access_token . '", "expires_time": ' . $expires_time . '}', FILE_USE_INCLUDE_PATH);
}


echo '{"access_token":"' . $access_token . '"}';


// 返回码	说明
// -1	系统繁忙，此时请开发者稍候再试
// 0	请求成功
// 40001	AppSecret错误或者AppSecret不属于这个公众号，请开发者确认AppSecret的正确性
// 40002	请确保grant_type字段值为client_credential
// 40164	调用接口的IP地址不在白名单中，请在接口IP白名单中进行设置。（小程序及小游戏调用不要求IP地址在白名单内。）
