import React from 'react';

const Home = () => {
  return (
    <div className="min-h-screen bg-black text-white p-8">
      <header className="mb-12">
        <h1 className="text-5xl font-extrabold tracking-tight bg-gradient-to-r from-purple-500 to-pink-500 bg-clip-text text-transparent">
          Smart E-Commerce Platform
        </h1>
        <p className="text-gray-400 mt-2 text-lg">
          Experience premium shopping powered by AI-driven personalization.
        </p>
      </header>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="glass-card p-6 rounded-2xl">
          <h3 className="text-xl font-bold mb-2">⚡ Flash Sales</h3>
          <p className="text-gray-400">Discover handpicked premium selections at up to 60% off.</p>
        </div>
        <div className="glass-card p-6 rounded-2xl">
          <h3 className="text-xl font-bold mb-2">🎁 Loyalty Rewards</h3>
          <p className="text-gray-400">Earn points on every purchase and redeem them instantly during checkout.</p>
        </div>
        <div className="glass-card p-6 rounded-2xl">
          <h3 className="text-xl font-bold mb-2">🔒 Secure Payments</h3>
          <p className="text-gray-400">Seamless payment integration with state-of-the-art Razorpay security.</p>
        </div>
      </div>
    </div>
  );
};

export default Home;
