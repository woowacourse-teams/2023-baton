import type { Meta, StoryObj } from '@storybook/react';
import Modal from './Modal';
import Button from '../Button/Button';
import React from 'react';

const meta = {
  title: 'common/Modal',
  component: Modal,
  parameters: {
    layout: 'centered',
  },
} satisfies Meta<typeof Modal>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: <Button colorTheme="RED">Button</Button>,
    closeModal: () => {},
  } as const,
};
