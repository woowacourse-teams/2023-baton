import Avatar from '@components/common/Avatar';
import Button from '@components/common/Button';
import Tag from '@components/common/Tag';
import Layout from '@layout/Layout';
import React from 'react';

const MainPage = () => {
  return (
    <Layout>
      <div></div>
      <Avatar
        imageUrl="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdwCcM7YK61yYeovAZr4pDoaO-KA6TYqQS8s4sn92mHQ&s"
        width={'60px'}
        height={'60px'}
      />
      <Tag>자바</Tag>
      <Button colorTheme="RED" fontSize="14px" fontWeight={400}>
        새로운 버튼
      </Button>
      <Button colorTheme="GRAY">기본 버튼</Button>
      <Button colorTheme="WHITE">기본 버튼</Button>
    </Layout>
  );
};

export default MainPage;
