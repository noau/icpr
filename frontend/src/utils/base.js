// 根据 URL 后缀判断文件类型
export function getFileTypeFromUrl(url) {
    // 获取文件的后缀名
    const getFileExtension = (url) => {
      const extension = url.split('.').pop().toLowerCase(); // 获取 URL 的后缀并转为小写
      return extension;
    };
  
    const fileExtension = getFileExtension(url);
  
    // 判断文件类型
    if (fileExtension === 'pdf') {
      return 1; // PDF 文件
    } else if (['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(fileExtension)) {
      return 2; // 图片文件
    } else if (['doc', 'docx'].includes(fileExtension)) {
      return 3; // Word 文件
    } else {
      return 0; // 其他类型文件
    }
}
