// src/components/ProductForm.js
import React, { useState } from 'react';
import axios from 'axios';

const ProductForm = ({ onProductSaved, existingProduct }) => {
    const [name, setName] = useState(existingProduct ? existingProduct.name : '');
    const [description, setDescription] = useState(existingProduct ? existingProduct.description : '');
    const [price, setPrice] = useState(existingProduct ? existingProduct.price : '');

    const handleSubmit = async (e) => {
        e.preventDefault();
        const product = { name, description, price };

        try {
            if (existingProduct) {
                // Update existing product
                await axios.put(`/api/products/${existingProduct.id}`, product);
            } else {
                // Create new product
                await axios.post('/api/products', product);
            }
            onProductSaved();
            setName('');
            setDescription('');
            setPrice('');
        } catch (error) {
            console.error('Error saving product', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>{existingProduct ? 'Edit Product' : 'Add New Product'}</h2>
            <div>
                <label>Name</label>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Description</label>
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Price</label>
                <input
                    type="number"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    step="0.01"
                    required
                />
            </div>
            <button type="submit">{existingProduct ? 'Update' : 'Add'} Product</button>
        </form>
    );
};

export default ProductForm;
