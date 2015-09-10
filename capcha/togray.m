for i=1:99
    img=imread([num2str(i) '.jpg'], 'jpg');
    img_gray=rgb2gray(img);
    %imwrite(img_gray, ['gray/',num2str(i),'.jpg'], 'jpg');
    
    % seperate four digits
    n1=img_gray(:,1:11);
    n2=img_gray(:,12:22);
    n3=img_gray(:,23:33);
    n4=img_gray(:,34:44);
    
    % turn image into black&white image
    n1=im2bw(n1,0.5);
    n2=im2bw(n2,0.5);
    n3=im2bw(n3,0.5);
    n4=im2bw(n4,0.5);
    
    % write image into file
    imwrite(n1, ['gray/',num2str(i),'_1.bmp']);
    imwrite(n2, ['gray/',num2str(i),'_2.bmp']);
    imwrite(n3, ['gray/',num2str(i),'_3.bmp']);
    imwrite(n4, ['gray/',num2str(i),'_4.bmp']);
    
    
end;