1.该工具开发基于Ubuntu kylin操作系统，使用Java语言和eclipse软件实现代码编辑与调试，并使用Windowsbuilder完成UI设计，软件的目标运行环境是Ubuntu kylin系统。

2.本软件是一类文字识别工具，主要实现将图片、扫描件和PDF等文件中的文字信息识别输出为文本格式，也可以根据用户需要对屏幕框选区域中的文字信息实现识别输出，以便于用户进一步对内容进行编辑、搜索、翻译、保存导出等操作。

3.本工具的普通识别和英文识别基于Tesseract实现， Tesseract是 Google 发布的一款 OCR 开源库，可以抓取图片中的文字，它支持多个编程语言环境，以及运行环境。Tesseract项目最初由惠普实验室支持，1996年被移植到Windows上，1998年进行了C++化。2006年到现在，都由Google公司开发，Tesseract被认为是最准确的开源OCR引擎之一。

4.本工具云识别基于百度ocr实现，由于作者能力有限，且近期较忙，本地识别准确率难以与百度ocr相媲美，所以电脑连接网络时优先使用百度ocr进行识别。没有网络时自动切换本地识别。本工具使用了一些技巧，实现免费的无限次的云识别功能。

5.本工具翻译功能引接于金山爱词霸线上翻译功能。

6.其他细节功能和技术细节，在PPT中有所展示，可以查看。

7.代码规范方面还需要进一步加强，前期开发没有良好规划，代码规范性和其他一些已知的bug将在后期更新完善。
