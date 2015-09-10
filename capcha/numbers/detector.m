i1=imread('1.jpg');
i2=imread('2.jpg');
i3=imread('3.jpg');
i4=imread('4.jpg');
i5=imread('5.jpg');

c1=corner(i1);
c2=corner(i2);
c3=corner(i3);
c4=corner(i4);
c5=corner(i5);

figure;
imshow(i1);
hold on;
plot(c1(:,1), c1(:,1), '*', 'Color', 'c')

figure;
imshow(i2);
hold on;
plot(c2(:,1), c2(:,2), '*', 'Color', 'c')

figure;
imshow(i3);
hold on;
plot(c3(:,1), c3(:,2), '*', 'Color', 'c')

figure;
imshow(i4);
hold on;
plot(c4(:,1), c4(:,2), '*', 'Color', 'c')

figure;
imshow(i5);
hold on;
plot(c5(:,1), c5(:,2), '*', 'Color', 'c')